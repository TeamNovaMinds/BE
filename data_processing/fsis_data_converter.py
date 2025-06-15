import json
import pandas as pd
from deep_translator import GoogleTranslator
import re
import time
import mysql.connector
from mysql.connector import Error

class FSISDataConverter:
    """
    FSIS 식품 데이터를 우리 데이터베이스 구조에 맞게 변환하는 클래스

    Excel 형태로 구조화된 JSON 데이터를 파싱하여
    우리가 사용하기 쉬운 딕셔너리 형태로 변환하는 기능을 포함합니다.
    """

    def __init__(self):
        """클래스 초기화"""
        self.translator = GoogleTranslator(source='en', target='ko')
        self.translation_cache = {}

        self.unit_to_days = {
            'Days': 1,
            'Weeks': 7,
            'Months': 30,
            'Years': 365
        }

        print("🔧 FSISDataConverter 초기화 완료 (Excel 형태 JSON 파싱 지원)")

    def parse_excel_style_data(self, raw_data):
        """
        Excel 스타일의 JSON 데이터를 딕셔너리 리스트로 변환하는 함수

        이 함수는 마치 흩어진 퍼즐 조각들을 하나의 완성된 그림으로
        맞추는 역할을 합니다. Excel에서 각 셀이 따로따로 변환된
        데이터를 하나의 의미 있는 레코드로 재구성합니다.

        입력 예시:
        [{"ID": 1.0}, {"Category_Name": "Baby Food"}, {"Subcategory_Name": null}]

        출력 예시:
        {"ID": 1.0, "Category_Name": "Baby Food", "Subcategory_Name": null}
        """
        parsed_data = []

        print(f"🔄 Excel 스타일 데이터 파싱 중... (총 {len(raw_data)}개 레코드)")

        for row_index, row in enumerate(raw_data):
            # 각 행이 리스트인지 확인
            if not isinstance(row, list):
                print(f"⚠️  행 {row_index}가 예상과 다른 형태입니다. 건너뜁니다.")
                continue

            # 행 내의 모든 딕셔너리를 하나로 병합
            merged_record = {}

            for cell in row:
                # 각 셀이 딕셔너리인지 확인
                if isinstance(cell, dict):
                    # 딕셔너리의 모든 키-값 쌍을 병합된 레코드에 추가
                    merged_record.update(cell)
                else:
                    print(f"⚠️  행 {row_index}의 셀이 딕셔너리가 아닙니다: {cell}")

            # 병합된 레코드가 비어있지 않으면 결과 리스트에 추가
            if merged_record:
                parsed_data.append(merged_record)

            # 진행률 표시 (매 100개마다)
            if (row_index + 1) % 100 == 0:
                print(f"   파싱 진행률: {row_index + 1}/{len(raw_data)}")

        print(f"✅ 파싱 완료: {len(parsed_data)}개의 유효한 레코드 생성")
        return parsed_data

    def translate_text(self, text):
        """영어 텍스트를 한국어로 번역하는 함수"""
        if not text or text in self.translation_cache:
            return self.translation_cache.get(text, text)

        if len(text) > 100:
            print(f"⚠️  텍스트가 너무 길어 번역 건너뜀: {text[:50]}...")
            return text

        try:
            time.sleep(0.2)
            translated = self.translator.translate(text)
            self.translation_cache[text] = translated
            print(f"✅ 번역 완료: {text} → {translated}")
            return translated

        except Exception as e:
            print(f"❌ 번역 실패 {text}: {e}")
            self.translation_cache[text] = text
            return text

    def convert_to_days(self, min_val, max_val, metric):
        """보관 기간을 일수로 변환하는 함수"""
        if not min_val or not max_val or not metric:
            return None

        try:
            avg_val = (float(min_val) + float(max_val)) / 2
            multiplier = self.unit_to_days.get(metric, 1)
            return int(avg_val * multiplier)
        except (ValueError, TypeError):
            return None

    def clean_ingredient_name(self, name, subtitle):
        """재료 이름을 정제하는 함수"""
        if not name:
            return None

        full_name = name
        if subtitle:
            full_name += f" ({subtitle})"

        cleaned = re.sub(r'[^\w\s가-힣(),]', '', full_name)
        cleaned = re.sub(r'\s+', ' ', cleaned).strip()
        return cleaned

    def process_categories(self, raw_categories_data):
        """
        카테고리 데이터를 처리하는 함수 (개선된 버전)

        이제 Excel 스타일의 데이터 구조를 제대로 처리할 수 있습니다.
        먼저 원시 데이터를 파싱한 후, 번역과 정제 작업을 수행합니다.
        """
        print(f"📂 카테고리 데이터 처리 시작...")

        # ✅ Excel 스타일 데이터를 먼저 파싱 (added)
        categories_data = self.parse_excel_style_data(raw_categories_data)

        processed_categories = []

        print(f"🔄 {len(categories_data)}개 카테고리 번역 및 정제 중...")

        for category in categories_data:
            # 이제 category는 올바른 딕셔너리 형태입니다
            category_name = category.get('Category_Name', '')
            subcategory_name = category.get('Subcategory_Name', '')

            # 빈 카테고리명은 건너뛰기
            if not category_name:
                continue

            # 서브카테고리가 있으면 결합
            if subcategory_name:
                full_category = f"{category_name} - {subcategory_name}"
            else:
                full_category = category_name

            # 한국어로 번역
            translated_name = self.translate_text(full_category)

            processed_categories.append({
                'id': category.get('ID'),
                'original_name': full_category,
                'korean_name': translated_name
            })

        print(f"✅ 카테고리 처리 완료: {len(processed_categories)}개")
        return processed_categories

    def process_products(self, raw_products_data, categories_dict):
        """
        제품 데이터를 처리하는 함수 (개선된 버전)

        카테고리와 마찬가지로 Excel 스타일의 제품 데이터를
        먼저 파싱한 후 번역과 변환 작업을 수행합니다.
        """
        print(f"🥕 제품 데이터 처리 시작...")

        # ✅ Excel 스타일 데이터를 먼저 파싱 (added)
        products_data = self.parse_excel_style_data(raw_products_data)

        processed_products = []

        print(f"🔄 {len(products_data)}개 제품 번역 및 변환 중...")

        for idx, product in enumerate(products_data, 1):
            if idx % 100 == 0:
                print(f"   진행률: {idx}/{len(products_data)} ({idx/len(products_data)*100:.1f}%)")

            # 제품명과 부제목 추출 및 정제
            name = product.get('Name', '')
            subtitle = product.get('Name_subtitle', '')
            cleaned_name = self.clean_ingredient_name(name, subtitle)

            if not cleaned_name:
                continue

            # 한국어로 번역
            korean_name = self.translate_text(cleaned_name)

            # 보관 기간 정보 추출 및 변환
            fridge_days = self.convert_to_days(
                product.get('DOP_Refrigerate_Min'),
                product.get('DOP_Refrigerate_Max'),
                product.get('DOP_Refrigerate_Metric')
            )

            freezer_days = self.convert_to_days(
                product.get('DOP_Freeze_Min'),
                product.get('DOP_Freeze_Max'),
                product.get('DOP_Freeze_Metric')
            )

            room_temp_days = self.convert_to_days(
                product.get('DOP_Pantry_Min') or product.get('Pantry_Min'),
                product.get('DOP_Pantry_Max') or product.get('Pantry_Max'),
                product.get('DOP_Pantry_Metric') or product.get('Pantry_Metric')
            )

            # 카테고리 정보 매핑
            category_id = product.get('Category_ID')
            category_info = categories_dict.get(category_id, {})

            processed_products.append({
                'original_name': cleaned_name,
                'korean_name': korean_name,
                'category_id': category_id,
                'category_korean_name': category_info.get('korean_name'),
                'fridge_days': fridge_days,
                'freezer_days': freezer_days,
                'room_temp_days': room_temp_days,
                'keywords': product.get('Keywords', '')
            })

        print(f"✅ 제품 처리 완료: {len(processed_products)}개")
        return processed_products

    def save_to_csv(self, data, filename):
        """처리된 데이터를 CSV 파일로 저장하는 함수"""
        df = pd.DataFrame(data)
        csv_path = f"data_processing/{filename}"
        df.to_csv(csv_path, index=False, encoding='utf-8-sig')
        print(f"📄 CSV 파일 저장 완료: {filename}")

    def generate_sql_inserts(self, categories, products):
        """SQL INSERT 문을 생성하는 함수"""
        sql_statements = []

        sql_statements.append("-- ========================================")
        sql_statements.append("-- FSIS 식품 데이터 자동 변환 결과")
        sql_statements.append("-- 생성일시: " + time.strftime("%Y-%m-%d %H:%M:%S"))
        sql_statements.append("-- Excel 스타일 JSON 파싱 지원")
        sql_statements.append("-- ========================================")
        sql_statements.append("")

        # 카테고리 INSERT 문 생성
        sql_statements.append("-- 1. 재료 카테고리 데이터 삽입")
        for category in categories:
            safe_name = category['korean_name'].replace("'", "''")
            sql = f"""INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('{safe_name}', 'APPROVED', NULL);"""
            sql_statements.append(sql)

        sql_statements.append("")
        sql_statements.append("-- 2. 재료 데이터 삽입")

        # 재료 INSERT 문 생성
        for product in products:
            safe_ingredient_name = product['korean_name'].replace("'", "''")
            safe_category_name = product['category_korean_name'].replace("'", "''") if product['category_korean_name'] else ''

            fridge = product['fridge_days'] if product['fridge_days'] is not None else 'NULL'
            freezer = product['freezer_days'] if product['freezer_days'] is not None else 'NULL'
            room_temp = product['room_temp_days'] if product['room_temp_days'] is not None else 'NULL'

            sql = f"""INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('{safe_ingredient_name}', {fridge}, {room_temp}, {freezer}, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '{safe_category_name}'), NULL);"""
            sql_statements.append(sql)

        sql_file_path = 'data_processing/fsis_data_inserts.sql'
        with open(sql_file_path, 'w', encoding='utf-8') as f:
            f.write('\n'.join(sql_statements))

        print(f"🗃️  SQL INSERT 문 생성 완료: fsis_data_inserts.sql")

def main():
    """메인 실행 함수"""
    print("🚀 FSIS 데이터 변환 시작...")
    print("🔄 Excel 스타일 JSON 파싱 지원 버전")
    print("=" * 50)

    converter = FSISDataConverter()

    # JSON 파일 로드
    try:
        json_file_path = 'data_processing/FSIS_FoodKeeper.json'
        with open(json_file_path, 'r', encoding='utf-8') as f:
            fsis_data = json.load(f)
            print("✅ JSON 파일 로드 성공")
    except FileNotFoundError:
        print("❌ FSIS_FoodKeeper.json 파일을 찾을 수 없습니다.")
        print("   현재 찾고 있는 경로:", json_file_path)
        return
    except json.JSONDecodeError as e:
        print(f"❌ JSON 파일 형식이 올바르지 않습니다: {e}")
        return

    # JSON 구조 검증 및 시트 추출
    if 'sheets' not in fsis_data:
        print("❌ JSON 파일에 'sheets' 데이터가 없습니다.")
        return

    categories_data = None
    products_data = None

    print("\n📋 JSON 파일 내부 시트 목록:")
    for sheet in fsis_data['sheets']:
        sheet_name = sheet.get('name', '이름없음')
        data_count = len(sheet.get('data', []))
        print(f"   - {sheet_name}: {data_count}개 레코드")

        if sheet_name == 'Category':
            categories_data = sheet['data']
            print("     ✅ Category 시트 선택됨")
        elif sheet_name == 'Product':
            products_data = sheet['data']
            print("     ✅ Product 시트 선택됨")
        else:
            print("     ⏭️  사용하지 않는 시트 (건너뜀)")

    if not categories_data or not products_data:
        print("\n❌ 필요한 데이터 시트를 찾을 수 없습니다.")
        return

    print(f"\n📊 처리할 데이터 통계:")
    print(f"   - 카테고리: {len(categories_data)}개")
    print(f"   - 제품: {len(products_data)}개")
    print(f"   - 예상 소요 시간: 약 {len(products_data) * 0.3 / 60:.1f}분")

    user_input = input("\n계속 진행하시겠습니까? (y/n): ").lower().strip()
    if user_input != 'y' and user_input != 'yes':
        print("작업이 취소되었습니다.")
        return

    print("\n" + "=" * 50)

    # 데이터 변환 작업 수행
    try:
        processed_categories = converter.process_categories(categories_data)
        categories_dict = {cat['id']: cat for cat in processed_categories}

        print()
        processed_products = converter.process_products(products_data, categories_dict)

        print("\n" + "=" * 50)
        print("💾 결과 파일 저장 중...")
        converter.save_to_csv(processed_categories, 'processed_categories.csv')
        converter.save_to_csv(processed_products, 'processed_products.csv')
        converter.generate_sql_inserts(processed_categories, processed_products)

        print("\n🎉 변환 작업 완료!")
        print("=" * 50)
        print(f"✅ 처리된 카테고리: {len(processed_categories)}개")
        print(f"✅ 처리된 제품: {len(processed_products)}개")

    except KeyboardInterrupt:
        print("\n\n⏹️  사용자에 의해 작업이 중단되었습니다.")
    except Exception as e:
        print(f"\n❌ 예상치 못한 오류가 발생했습니다: {e}")
        import traceback
        traceback.print_exc()

if __name__ == "__main__":
    main()