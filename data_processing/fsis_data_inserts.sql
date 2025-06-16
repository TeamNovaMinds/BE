-- ========================================
-- FSIS 식품 데이터 자동 변환 결과
-- 생성일시: 2025-06-15 00:06:59
-- Excel 스타일 JSON 파싱 지원
-- ========================================

-- 1. 재료 카테고리 데이터 삽입
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('유아식', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('구운 제품 - 빵집', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('구운 식품 - 베이킹과 요리', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('구운 제품 - 냉장 반죽', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('음료수', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('조미료, 소스 및 통조림', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('유제품 및 계란', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('냉동 식품', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('곡물, 콩 및 파스타', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('고기 - 신선한', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('고기 - 선반 안정적인 음식', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('고기 - 훈제 또는 가공', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('고기 - 박제 또는 조립', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('가금류 - 요리 또는 가공', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('가금류 - 신선한', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('가금류 - 선반 안정적인 음식', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('가금류 - 박제 또는 조립', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('생산 - 신선한 과일', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('생산 - 신선한 야채', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('해산물 - 신선한', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('해산물 - 조개', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('해산물 - 훈제', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('선반 안정적인 음식', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('채식 단백질', 'APPROVED', NULL);
INSERT INTO ingredient_category (ingredient_category_name, status, user_id)
                     VALUES ('델리 & 준비된 음식', 'APPROVED', NULL);

-- 2. 재료 데이터 삽입
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('버터', 45, NULL, 225, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('버터 밀크', 10, NULL, 90, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('치즈 (체다, 스위스, 블록 파마산과 같은 하드)', 180, NULL, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('치즈 (파마산 파쇄 또는 강판)', 360, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('치즈 (파쇄 된 체다, 모짜렐라 등)', 30, NULL, 105, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('치즈 (가공 슬라이스)', 24, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('치즈 (Brie, Bel Paese, 염소와 같은 부드러운)', 10, NULL, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('커피 크리머 (액체 냉장)', 21, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('코티지 치즈', 14, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크림 치즈', 14, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크림 (휘핑, 울트레이 스토퍼 화)', 30, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크림 (휘핑, 달콤한)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크림 (반과 반)', 3, NULL, 120, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크림 (무거운)', 10, NULL, 105, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크림 (빛)', 7, NULL, 105, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('딥 (사워 크림 기반)', 14, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('계란 대체물 (액체)', 7, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('에그 노그 (상업)', 4, NULL, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('계란 (쉘)', 28, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('계란 (생백, 노른자)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('계란 (단단한 삶은 요리)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('계란 요리', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('케 피어 (발효 우유)', 7, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('마가린', 180, NULL, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('우유 (평범하거나 맛)', NULL, NULL, 90, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('푸딩', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크림', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('휘핑 크림 (에어로졸 캔)', 24, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('휘핑 토핑 (에어로졸 캔)', 90, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('휘핑 토핑 (욕조)', 14, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('요구르트', 10, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쇠고기 (갈비 로스트, 뼈)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쇠고기 (늑골 로스트 뼈없는, 롤링)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쇠고기 (둥근 또는 엉덩이 로스트)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쇠고기 (enderloin, 전체)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쇠고기 (enderloin, 반)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쇠고기 (척 로스트, 브리 스킷)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쇠고기 (스테이크)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쇠고기 (스튜, 큐브)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쇠고기 (짧은 갈비)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쇠고기 (지상)', 1, NULL, 105, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양고기 (다리, 뼈가 작은)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양고기 (다리, 대다', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양고기 (다리가없는, 롤링)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양고기 (크라운 로스트)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양고기 (어깨 로스트 또는 생크 다리 반)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양고기 (Kabobs 용 큐브)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양고기 (지상)', 1, NULL, 105, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양고기 (갈비, 갈비 또는 허리)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양고기 (다리 스테이크)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양고기 (스튜 고기, 조각)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양고기 (생크)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양고기 (유방, 롤링)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('송아지 고기 (어깨 로스트, 뼈가없는)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('송아지 고기 (다리 엉덩이 또는 둥근 로스트, 뼈가없는)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('송아지 고기 (허리 로스트, 뼈)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('송아지 (ground)', 1, NULL, 105, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 고기 (허리 로스트, 뼈)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 고기 (허리 로스트, 뼈없는)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 고기 (허리 자질, 뼈)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 고기 (허리 자르기, 뼈없는)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 고기 (크라운 로스트 작은)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 고기 (크라운 로스트 대형)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 고기 (텐더 로인)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 고기 (텐더 로인 메달리온 또는 허리 큐브)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('염소 (부드러운 컷 (다리, 갈비, 어깨, 허리))', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('염소 (덜 부드러운 컷 (스튜 고기, 리블릿, 생크)))', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 고기 (갈비)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 고기 (지상)', 1, NULL, 105, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 고기 (어깨)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 고기 (허리, 큐브)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('사슴 고기 (둥근, 엉덩이, 허리 또는 갈비 로스트)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('다양한 고기 (간, 혀, 치터 링 등)', 1, NULL, 105, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('염소 (지면)', 1, NULL, 105, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('베이컨', 7, NULL, 30, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('콘디 쇠고기 (산세 주스가있는 파우치)', 6, NULL, 30, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (통조림) (냉장 레이블 유지)', 225, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (완전히 요리, Benein, 전체)', 7, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (완전히 요리 된, 뼈, 반)', 7, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (완전히 요리, 슬라이스, 반 또는 나선 절단)', 3, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (완전히 요리, 팔 피크닉 어깨, 뼈가없는)', 7, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (완전히 요리, 통조림, 뼈가없는 (냉장))', 225, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (완전히 요리, 진공 포장, 뼈가없는)', 14, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (신선한, Cookbeforeing, Benein, 전체)', 7, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (신선한, 요리사, 뼈, 반)', 7, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (신선한, 요리사, 생크 또는 엉덩이 부분, 뼈)', 7, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (신선한, 요리사, 팔 피크닉 어깨, 뼈가없는)', 7, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (신선한, Cookbeforeing, 어깨 롤 (엉덩이), Boneless)', 7, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (신선하고, 요리되지 않은, 다리 전체, 뼈)', 7, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (신선하고, 요리되지 않은, 다리 전체, 뼈가없는)', 7, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (신선하고, 요리되지 않은, 반 다리, 뼈)', 7, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (Country Ham (전체 또는 절반))', 7, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('핫도그', 14, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('소시지 (원시 벌크 유형 또는 패티)', 1, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('소시지 (완전히 요리 된 훈제 링크, Kielbasa)', 7, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('소시지 (단단하고 건조한 (페퍼로니), 얇게 썬)', 17, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('박제 된 생 돼지 갈비', 1, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 박제 또는 조립'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('야채를 곁들인 생 카밥', 1, NULL, 105, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 박제 또는 조립'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('베이컨 (완전히 요리)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (shelfstable cans)', NULL, 225, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('육포 (상업적으로 말린)', NULL, 360, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('육포 (수제)', NULL, 45, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('고기 제품 (통조림)', NULL, 1825, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('레토르트 파우치 또는 상자', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('닭 (전체)', 1, NULL, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('터키 (전체)', 1, NULL, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('지상 칠면조 또는 닭고기', 1, NULL, 105, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('닭 부분 (가슴 반쪽, 뼈)', 1, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('닭 부분 (가슴 반쪽, 뼈없는)', 1, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('닭 부분 (다리 또는 허벅지)', 1, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('칠면조 부품 (가슴 반쪽, 뼈)', 1, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('칠면조 부품 (가슴 반쪽, 뼈없는)', 1, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('칠면조 부품 (다리 또는 허벅지)', 1, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('오리 링 (국내 또는 야생, 전체)', 1, NULL, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('거위 (국내 또는 야생, 전체)', 1, NULL, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('꿩 (젊고 전체)', 1, NULL, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('메추라기 (전체)', 1, NULL, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('카폰 (전체)', 1, NULL, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('콘월 어 암탉 (전체)', 1, NULL, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('내장', 1, NULL, 105, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('박제 된 생 닭 가슴', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 박제 또는 조립'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('야채를 곁들인 생 카밥', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 박제 또는 조립'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('투르 켄', NULL, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 박제 또는 조립'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('치킨 너겟, 패티', NULL, NULL, 60, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 요리 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('요리 된 가금류 요리', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 요리 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('닭 튀김', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 요리 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('가금류 조각 (그레이비 또는 국물로 덮여 있음)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('로티시스 치킨', 3, NULL, 120, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 요리 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('통조림 닭', NULL, 1825, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('레토르트 파우치 또는 상자', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('마른 물고기 (대구, under, haddock, halibut, sole 등)', 1, NULL, 210, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('마른 물고기 (Pollock, Ocean Perch, Rockfish, Sea Trout)', 1, NULL, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('지방 생선 (블루 피쉬, 메기, 고등어, 숭어, 연어, 참치 등)', 1, NULL, 75, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('캐비어 (신선한, 항아리)', 17, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('요리 된 물고기 (모두)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('수리미 해산물', NULL, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('가리비', 2, NULL, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 조개'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('새우, 가재', 2, NULL, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 조개'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('오징어', 2, NULL, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 조개'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('조개, 홍합 및 굴', 6, NULL, 105, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 조개'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('게 고기 (신선한)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 조개'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('게 고기 (저온 살균)', 330, NULL, 210, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 조개'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('게 다리 (왕, 배설물, 눈)', 3, NULL, 315, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 조개'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('살아있는 조개, 홍합, 게 및 굴', 1, NULL, 75, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 조개'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('신선한 랍스터 (라이브)', 1, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 조개'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('신선한 랍스터 꼬리 (요리)', 2, NULL, 21, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 조개'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('신선한 조개, 홍합, 굴', 7, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 조개'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('신선한 랍스터 (냉동)', 1, NULL, 21, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 조개'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('요리 된 조개류 (모두)', NULL, NULL, 60, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 조개'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('청어 (유리 포장, 와인 소스)', 150, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 훈제'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('생선 (뜨거운 훈제, 공기 팩)', 29, NULL, 315, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 훈제'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('생선 (뜨거운 훈제, 진공 팩)', 29, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 훈제'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('생선 (차가운 훈제, 공기 팩)', 22, NULL, 315, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 훈제'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('생선 (차가운 훈제, 진공 팩)', 25, NULL, 315, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '해산물 - 훈제'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('두부', 7, NULL, 150, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '채식 단백질'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('된장', 365, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '채식 단백질'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('대두 가루 (fullfat)', 180, 60, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '채식 단백질'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('질감 대두 단백질 ((TSP))', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '채식 단백질'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('재수 화 된 질감 대두 단백질 ((TSP))', NULL, 105, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '채식 단백질'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('남은 음식 (고기, 생선, 가금류 또는 계란 포함)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('남은 음식 (고기, 생선, 가금류 또는 계란없이 (예 : 요리 된 야채, 쌀 또는 감자)))', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('남은 음식 (피자)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('상업용 브랜드 진공 저녁 식사 (USDA SEAL 포함)', 14, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('요리 된 파스타', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('요리 된 쌀', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('과일, 잘라', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아보카도 소스', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Hummus (상업용 (보존, 방부제와 함께))', 90, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Hummus (전통적 (방부제 없음, 저온 살균되지 않음))', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('오찬 고기 또는 가금류 (StoreSliced)', 4, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('주요 요리 또는 식사 (뜨거운 또는 냉장)', 3, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('고기 (그레이비 또는 국물로 덮여)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('올리브 (올리브 바에서)', 14, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('머리', 1, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('오찬 고기 또는 가금류 (사전 포장)', 14, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('수프, 스튜', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('캐서롤', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('상업용 빵 제품 (팬 빵, ​​평평한 빵, 롤 및 빵 포함)', NULL, 16, 120, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('옥수수 (밀가루)', NULL, 90, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('상업용 케이크와 머핀', NULL, 5, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('치즈 케이크', NULL, NULL, 135, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쿠키 (소프트)', NULL, 75, 300, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쿠키 (바삭한)', NULL, 150, 300, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('유제품이 채워진 eclairs', NULL, NULL, 90, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('도넛', NULL, 1, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('과일 케이크', NULL, 180, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('패스트리, 덴마크', NULL, 7, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크림 파이 (바나나 크림, 코코넛 크림, 버터 스카치)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쉬폰 파이 (초콜릿, 레몬)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('과일 파이 (사과, 복숭아, 블루 베리, 살구, 체리, 블랙 베리)', NULL, 1, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('파이 (MinceMeat)', NULL, 2, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('파이 (피칸)', NULL, 2, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('파이 (호박)', NULL, 2, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('quiche', NULL, 2, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('베이킹 파우더', NULL, 360, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('베이킹 소다', NULL, 912, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('비스킷 또는 팬케이크 믹스', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('케이크, 브라우니, 빵 믹스', NULL, 450, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('초콜릿 (무섭고 반 달콤함, 단단한)', NULL, 547, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('코코아와 코코아 믹스', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('옥수수 가루 (정기, 탈출)', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('옥수수 가루 (돌 지대 또는 파란색)', NULL, 30, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('콘스타치', NULL, 630, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('밀가루 (흰색)', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('밀가루 (통 밀)', NULL, 135, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('프로스팅 또는 착빙', NULL, 330, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('젤라틴 (풍미)', NULL, 330, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('젤라틴 (플라스틱)', NULL, 1095, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('기름 (올리브 또는 야채)', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('너트 오일', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('식물성 기름 스프레이', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('단축 (고체)', NULL, 547, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('타마 린드 페이스트', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('칠리 가루', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('조미료 블렌드', NULL, 547, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('맛 또는 허브 믹스', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('마늘 (다진, 상업용 항아리)', NULL, 300, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('허브 (말린)', NULL, 547, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Spicespices (전체)', NULL, 1277, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Spicespices (지상)', NULL, 912, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('설탕 (갈색)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('설탕 (과립)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('설탕 (제과)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('설탕 대체물', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('타피 오카', NULL, 360, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('튜브 캔 (비스킷, 롤, 피자 반죽 등)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 냉장 반죽'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('ReadyTobake 파이 크러스트', NULL, NULL, 60, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 냉장 반죽'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쿠키 반죽', NULL, NULL, 60, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 냉장 반죽'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('사과', 35, 21, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('살구', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아보카도', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('바나나', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('딸기 (체리, 거위 열매, 리치)', 7, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('베리 (Blackberries, Boysenberries, Currant)', 4, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('블루 베리', 10, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Cherimoya', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('감귤류 과일 (레몬, 라임, 오렌지, 자몽, 귤, 클레멘 타인)', 15, 10, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('코코넛 (파쇄)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('코코넛 (신선한)', 17, 7, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크랜베리', 60, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('날짜', 360, 60, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('포도', 7, 1, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('구아바', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('키위 과일', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('멜론', 14, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('파파야, 망고, 페이 조아, Passionfruit, Casaha Melon', 7, 4, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('복숭아, 천도, 자두, 배, 사트', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('파인애플', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('질경이', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('석류', 60, 3, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아티 초크, 전체', 10, 1, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아스파라거스', 3, NULL, 150, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('대나무 싹', 14, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('콩과 완두콩 (녹색, 파바, 리마, 콩, 왁스, 눈, 설탕 스냅)', 4, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('사탕무', 10, 1, 210, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Bok Choy', 2, NULL, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('브로콜리와 브로콜리 라브 (라피니)', 4, NULL, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('브뤼셀 콩나물', 4, NULL, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양배추', 10, NULL, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('당근, 파스 니프', 17, NULL, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('콜리 플라워', 4, NULL, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('셀러리', 10, NULL, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('개 암 나무 옥수수', 1, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('오이', 5, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('가지', 5, 1, 210, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('마늘', 8, 30, 30, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('생강 뿌리', 17, 3, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('푸성귀', 2, NULL, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('허브', 8, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('부추', 10, NULL, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양상추 (빙산, 로메인)', 10, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양상추 (잎, 시금치)', 5, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('버섯', 5, NULL, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('오크라', 2, 1, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양파 (노란색, 흰색, 빨간색 등)', 60, 30, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양파 (봄 또는 녹색)', 7, 30, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('고추', 9, NULL, 210, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('감자', 10, 45, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('호박', 120, 75, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('무당', 12, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('대황', 5, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Rutabagas', 17, 7, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('스쿼시 (여름과 호박)', 4, 3, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('스쿼시 (겨울)', 60, 28, 330, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('타마 린드', 180, 14, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('타로', 2, 7, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('토마토', NULL, NULL, 60, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('순무', 14, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('유카사스사함', 3, 7, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('반죽 (상업용 (빵 또는 쿠키))', NULL, NULL, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('계란 대체물', NULL, NULL, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('물고기 (빵가루)', NULL, NULL, 540, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('생선 (생식이지만 향하고 gutted)', NULL, NULL, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('냉동 감자 제품 (튀김, 해시 브라운, 테이터 토트)', NULL, NULL, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('냉동 프레즐', NULL, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('과일 (예 : 딸기, 멜론)', NULL, NULL, 420, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아이스크림', NULL, NULL, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('얼음 팝', NULL, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('주스 농축 물', NULL, NULL, 730, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('랍스터 꼬리', NULL, NULL, 240, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('팬케이크, 와플', NULL, NULL, 90, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('소시지 (요리되지 않은)', NULL, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('소시지 (사전 조리)', NULL, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Sherbet, 셔벗', NULL, NULL, 270, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('새우, 조개류', NULL, NULL, 450, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('간장 부서와 핫도그', NULL, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('간장 고기 대체물', NULL, NULL, 450, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('템페', NULL, NULL, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('냉동 앙트레 (준비된 식사)', NULL, NULL, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('채소', NULL, NULL, 420, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('신선한 파스타', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('콩 (말린)', NULL, 547, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('렌즈 콩 (건조)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('파스타 (계란없이 건조)', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('마른 계란 국수', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('완두콩 (말린 분할)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쌀 (흰색 또는 야생)', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쌀 (갈색)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('바베큐 소스 (병)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('처트니', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크림 소스, 우유 고체', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('건조한 그레이비 믹스', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('그레이비 (항아리와 캔)', NULL, 1277, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('꿀', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양 고추 냉이 (항아리)', 360, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('잼, 젤리 및 보존', NULL, 360, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('케첩, 칵테일 또는 칠리 소스', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('매리 네이드', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('마요네즈 (상업)', NULL, 135, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('겨자', NULL, 547, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('올리브 (검은 색과 녹색)', NULL, 450, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('절인 것', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('페스토 (Jarred)', 180, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('샐러드 드레싱 (상업용, 병에 든)', NULL, 330, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('살사 (Picante and Taco 소스)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('소스 믹스 (nondairy (스파게티, 타코 등))', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('스파게티 소스 (항아리)', NULL, 540, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('간장 또는 테리야키 소스', NULL, 1095, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('식초', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('우스터 셔 소스', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('항아리 또는 파우치', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유아식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('과일', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유아식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('채소', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유아식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('저녁 식사', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유아식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('시리얼, 마른 믹스', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유아식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('공식 (준비)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유아식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('액체 농축 물 또는 기성적 인 공식', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유아식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('사과 소스 (상업)', NULL, 450, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('베이컨 비트 (모방)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Canned goods (low acid (such as meat, poultry, fish, gravy, stew, soups, beans, carrots, corn, pasta, peas, potatoes, spinach))', NULL, 1277, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Canned goods (high acid (such as juices, fruit, pickles, sauerkraut, tomato soup, and foods in vinegarbased sauce))', NULL, 450, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('시리얼 (readytoeat)', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('시리얼 (먹기 전 요리 (오트밀 등))', NULL, 360, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('초콜릿 시럽', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('호두 까는 기구', NULL, 240, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Graham Crackeranimal Cracker', NULL, 225, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('과일, 말린 (건포도, 살구, 망고, 크랜베리 ​​등)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('구미 (과일) 간식', NULL, 225, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('마시멜로', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('마시멜로 크림', NULL, 105, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('우유 (통조림 증발 또는 응축)', NULL, 360, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('당밀', NULL, 547, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('버섯 (말린)', NULL, 547, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('견과류 (항아리 또는 캔)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('땅콩 버터 (상업)', NULL, 450, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('펙틴', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('팝콘 (JAR의 마른 커널)', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('팝콘 (가방에 상업적으로 튀어 나온)', NULL, 75, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('팝콘 (전자 레인지 패킷)', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('감자 칩', NULL, 60, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('감자 (순간)', NULL, 375, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('프레즐', NULL, 195, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('푸딩 믹스', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('수프 믹스 (드라이 부릴론)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('태양 말린 토마토 (jar)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('시럽', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('토스터 패스트리', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('커피 (전체 콩)', NULL, 120, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('커피 (홈 그라운드, 비 바쿠움)', NULL, 120, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('커피 (즉석)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('다이어트 파우더 믹스 및 음료 믹스', NULL, 630, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('상자, 과일 음료, 펀치의 과일 주스', NULL, 21, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('주스, 상자', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('넥타 (파파야, 망고, 구아바 또는 구아바나)', NULL, 450, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('소다 (탄산 콜라 음료, 믹서, 다이어트 소다 병 또는 캔)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('간장 또는 쌀 음료', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('차 (가방)', NULL, 810, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('차 (즉석)', NULL, 912, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('차 (느슨한)', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('물 (상업적으로 병에 든)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Kumquats', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('포장 그린 (잎, 시금치, 양상추 등)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('타 히니', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('계란 샐러드', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('감자 샐러드', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('해산물 샐러드 (참치 샐러드, 새우 샐러드, 연어 샐러드, 혼합 해산물 샐러드 등)))', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('치킨 샐러드', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 샐러드', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('파스타 샐러드', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('콩 우유', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Yamssweet 감자', NULL, 17, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('케일', 4, NULL, 300, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('퀴 노아 (요리되지 않은)', NULL, 912, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('퀴 노아 (요리)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아몬드 우유', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쌀 우유', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('코코넛 밀크', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('커피 크리머 (파우더)', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('터키 베이컨', 14, NULL, 90, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 요리 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('과일 칵테일 (통조림)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('검은 콩 소스', NULL, 630, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('굴 소스', NULL, 630, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('호이신 소스', NULL, 630, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 롤', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아몬드 (쉘)', 480, 180, 600, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아몬드 (껍질 없음)', 240, 120, 300, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('캐슈 (쉘 없음)', 180, 21, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('마카다미아 (껍질 없음)', 360, 21, 720, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('땅콩 (쉘)', 360, 120, 720, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('땅콩 (껍질 없음)', 360, 28, 720, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('피칸 (쉘)', 360, 21, 720, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('피칸 (껍질 없음)', 270, 21, 720, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('피스타치오 (쉘 또는 쉘 없음)', 360, 21, 720, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('호두 (쉘 또는 껍질 없음)', 315, 21, 720, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('라임 주스', NULL, 450, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('레몬 주스', NULL, 450, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('베이글 (신선한 구운)', NULL, 1, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('베이글 (상업적으로 냉동)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('머핀 (밀기울, 블루 베리, 바나나, 옥수수, 초콜릿 칩을 포함한 수제)', NULL, 5, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('머핀 (상업적으로 포장)', NULL, 5, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('머핀 (믹스, 건조, 상업적 포장)', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('코코넛 오일', NULL, 1095, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('오렌지 주스 (상업적으로 포장 된 상자)', NULL, NULL, 300, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('오렌지 주스 (상업적으로 냉동 농축액)', NULL, NULL, 360, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('오렌지 주스 (갓 짜낸)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('구운 고추 (jar)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('통 밀가루', NULL, 360, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('통 밀 빵 (수제)', NULL, 4, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('통 밀 빵 (상업적으로 구운 소모)', NULL, 4, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('적포도주', NULL, 1460, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('백포도주', NULL, 547, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('드라이 스터핑 믹스 (상업적으로 포장)', NULL, 315, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('분유', NULL, 1460, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아몬드 버터', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('캐슈 버터', NULL, 90, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('소금 (표, 평원 또는 요오드화)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('검은 후추 (지면, 말린, 상업적으로 병에 든 또는 대량으로 구입)', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('검은 후추 (금이 간, 건조, 상업적으로 병에 든 또는 벌크로 구입)', NULL, 912, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('케이준 조미료 블렌드 (지면, 건조, 상업적으로 병에 든 또는 벌크로 구매)', NULL, 547, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('계피 (지면, 건조, 상업적으로 병에 든 또는 대량으로 구매)', NULL, 1277, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('커민 (지상, 건조, 상업적으로 병에 든 또는 대량으로 구입, 개방 또는 개봉)', NULL, 1277, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('마늘 가루 (건조, 상업적으로 병에 든 또는 대량으로 구입)', NULL, 1277, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양파 가루 (건조, 상업적으로 병에 든 또는 대량으로 구입)', NULL, 1277, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Nutmeg (지면, 건조, 상업적으로 병에 든 또는 대량으로 구매)', NULL, 1277, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('나초 치즈 (매장 구입)', NULL, 730, 90, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('스타 과일', 10, 7, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('가시 배', 10, 3, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('피타 야드 라곤 과일', 17, 3, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('딸기', 2, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('라즈베리', 2, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('체리', 2, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('국물 (수제)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Beef Brothstockconsommé (상업적으로 생산)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('닭고기 브로스 스톡 콘월메 (상업적으로 생산)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('야채 스톡 브로스 (상업적으로 생산)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('땅콩 버터 (수소화 지방 또는 기타 안정제를 함유 한 상업적으로 생산됨)', 360, 225, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('땅콩 버터 (천연)', 360, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('리코 타', 14, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아기 당근', 28, 4, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('JICAMA (신선한)', 17, 120, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('김치', 270, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Kohlrabi', 7, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('수박', 3, 1, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('멜론', 10, 3, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('단물', 3, 4, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('땅콩 (삶은, 껍질)', NULL, 1095, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('재 설인 콩', NULL, 1095, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('맛', 270, 900, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('토마토 페이스트', NULL, 810, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('태전자', 270, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아마씨 (전체 씨앗)', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아마씨 (지상)', 360, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('레몬 그라스', 8, NULL, 180, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('실란트로', NULL, 10, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('박하', NULL, 10, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('바질 (말린)', NULL, 360, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('바질 (신선한)', NULL, 5, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('오레가노', NULL, 10, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('로즈마리', NULL, 10, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('향신료', NULL, 10, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('백리향', NULL, 10, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('칠면조 (사전 패키지, 오찬 오데 델리 고기)', 14, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (사전 패키지, 오찬 오데 델리 고기)', 14, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('살라미 (사전 패키지, 오찬 오데 델리 고기)', 14, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('닭고기 (사전 패키지, 오찬 오데 델리 고기)', 14, NULL, 45, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('캐나다 베이컨 (전체)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('캐나다 베이컨 (슬라이스)', 80, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('카놀라유', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아몬드 오일', 730, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('해바라기 오일', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('포도 적 기름', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('오리 지방', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('베이컨 그리스', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('프라이드 오일 (재사용)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아몬드 추출물', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('계피 추출물', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('레몬 추출물', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('순수한 바닐라 추출물', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('버터 맛', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('코코넛 맛', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('진정한 메이플 시럽 (개봉, 유리)', NULL, 1460, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('진정한 메이플 시럽 (개봉되지 않은 플라스틱)', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('레몬 주스 (신선한 압박)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('사과 주스 (신선한 압박)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('당근 주스 (신선한 압박)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('하드 주류', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크림 주류 (개봉되지 않은)', NULL, 210, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('커피 (상업 지상, 비 바쿠움)', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('마카롱 (프랑스어)', NULL, 1, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('우유 (유당)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('끈 치즈', 150, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('비건 채식 체다 치즈', 120, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('쿼크 (신선한 치즈)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('호박 (신선한, 전체)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('고추', 7, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('콩나물', 7, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('스위스 차드', 10, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('퍼프 페이스트리', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 냉장 반죽'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('비스킷 (냉장)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 냉장 반죽'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('옥수수 (옥수수)', 75, 35, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('파이 크러스트 (냉장)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 냉장 반죽'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('그라 놀라', NULL, 225, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('돼지 고기 껍질', NULL, 120, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('사과 사이다 식초', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('영양 보충 음료 (통조림)', NULL, 1277, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('매운 소스', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('타이 붉은 카레 페이스트', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('효모 (포장)', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('사과 소스 (수제)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 과일'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('페이트 (고기)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('페이트 (가금류)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '가금류 - 요리 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크랜베리 소스 (통조림)', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크랜베리 소스 (수제)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('야채 주스 (shelfstable)', NULL, 540, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('야채 주스 (상업용, 냉장 판매)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('야채 주스 (수제)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('절인 야채 (기름)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('피자 (냉동)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('살사 (신선한)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('치아 씨앗', NULL, 540, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('빵 (수제)', NULL, 4, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 빵집'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아마란드 (곡물)', NULL, 120, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아마란드 (밀가루, 식사)', NULL, 60, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('보리 (통 곡물)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('보리 (밀가루, 식사)', NULL, 90, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('메밀 (통 곡물)', NULL, 60, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('메밀 (밀가루, 식사)', NULL, 30, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('파로 (통 곡물)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Farro (밀가루, 식사)', NULL, 90, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('기장 (곡물)', NULL, 60, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('기장 (밀가루, 식사)', NULL, 30, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('귀리 (통 곡물)', NULL, 120, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('귀리 (밀가루, 식사)', NULL, 60, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('호밀 (통 곡물)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('호밀 (밀가루, 식사)', NULL, 90, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('수수 (통 곡물)', NULL, 120, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('수수 (밀가루, 식사)', NULL, 60, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('철자 (통 곡물)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('철자 (밀가루, 식사)', NULL, 90, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('테프 (통 곡물)', NULL, 120, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('테프 (밀가루, 식사)', NULL, 60, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('토끼 (전체, 신선한)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('스파게티 스쿼시 (전체)', NULL, 60, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('스파게티 스쿼시 (컷)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('가람 마살라', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('토마토 소스 (스파게티, 피자)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('체리 토마토', NULL, 10, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('코코넛 크림 (통조림)', NULL, 1277, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('코코넛 밀크 (통조림)', NULL, 1277, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('양배추 샐러드 (수제, 준비)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('호박 씨앗 (생, 껍질 없음)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('호박 씨앗 (생, 인셔)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('호박 씨앗 (구운, 노 셀)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('호박 씨앗 (구운, 인셔)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('해바라기 씨앗 (생, 무감각, 껍질 없음)', NULL, 360, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('해바라기 씨앗 (구운, 껍질 없음)', NULL, 360, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('해바라기 씨앗 (구운, 소금에 절인 껍질)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('파슬리 (신선한)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('참깨 오일', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('참깨 씨앗', NULL, 1825, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('케이 퍼 (항아리)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('초콜릿 Hazlenut 스프레드', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('구운 견과류 (땅콩, 캐슈, 아몬드)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('참치 (패킷)', NULL, 540, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('참치 (통조림)', NULL, 1095, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('해산물 (통조림)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('피 멘토 치즈 (포장)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('들소 (전체)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('들소 (지상)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('샐러드 드레싱 (크림)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('샐러드 드레싱 (비네 그레트, 수제)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('샐러드 드레싱 (비네 그레트)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('샐러드 드레싱 (건조, 포장)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('그릿 (박스형)', NULL, 360, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('치즈 커드 (신선하고 미확인)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('야채 수프 (수제)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('chorizo ​​(신선한)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('chorizo ​​(훈제 및 가공, 요리되지 않은)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('chorizo ​​(훈제 및 가공, 요리)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('사탕무 (절인)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('계피 롤 (unbaked, 튜브)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 제품 - 냉장 반죽'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('계피 롤 (구운)', NULL, 2, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('요리 와인', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('불가르', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Bratwurst (신선한)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Bratwurst (담배 및 가공)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('에다메 (신선한)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('에다메 (냉동)', NULL, NULL, 730, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('에다 메 (Edamame) (소금에 절인, 드라이 로스트, 포드에서)', NULL, 360, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('빵 부스러기 (상업)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('우유 (shelfstable)', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('살사 (수제, 신선한)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('살사 (수제, 통조림)', NULL, 315, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('토마토 소스 (수제, 통조림)', NULL, 315, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('바베큐 소스 (수제, 통조림)', NULL, 315, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('버터 기름', NULL, 210, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '유제품 및 계란'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('옥수수 시럽', NULL, 1095, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('영양 보충 음료 (병)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('인스턴트 아침 음료 (병)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('소나무 너트', NULL, 17, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('코코넛 가루', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '구운 식품 - 베이킹과 요리'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('피칸 (다진)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('폴렌타 (shelfstable)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('폴렌타 (냉동)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('시리얼 또는 그라 놀라 바', NULL, 270, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('우유 (울트라 테아 살)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('코코넛 워터', NULL, 360, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('셀러리 뿌리', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('사과 사이다 (신선한)', NULL, 2, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('유주 주스', 315, 150, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('유주 (과일, 신선함)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '음료수'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('파스트 라미', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Kugel (상업)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '냉동 식품'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Kugel (수제)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '델리 & 준비된 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('아보카도 오일', NULL, 730, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('발사믹 식초', NULL, 1460, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Aioli (수제)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('샐러드 드레싱 (수제)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('베이스 (쇠고기, 야채)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('멸치 (통조림)', NULL, 1825, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '조미료, 소스 및 통조림'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('Radicchio', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('prosciutto', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (uncured, 신선, 요리)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('햄 (uncured, 신선, 요리되지 않은)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 신선한'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('arugula', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '생산 - 신선한 야채'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('녹두 (새싹)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('녹두 (건조, 식량 등급 가방)', NULL, 365, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('녹두 (건조, 진공 청소기)', NULL, 3285, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '곡물, 콩 및 파스타'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('크루통', NULL, 165, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('베이컨 (uncured)', NULL, NULL, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '고기 - 훈제 또는 가공'), NULL);
INSERT INTO ingredients (ingredient_name, fridge_days, room_temp_days, freezer_days, status, ingredient_category_id, user_id)
                     VALUES ('머스타드 (말린, 땅)', NULL, 180, NULL, 'APPROVED',
                     (SELECT id FROM ingredient_category WHERE ingredient_category_name = '선반 안정적인 음식'), NULL);