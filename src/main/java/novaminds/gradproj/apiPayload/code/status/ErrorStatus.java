package novaminds.gradproj.apiPayload.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import novaminds.gradproj.apiPayload.code.BaseErrorCode;
import novaminds.gradproj.apiPayload.code.ErrorReasonDTO;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // For test
    TEMP_EXCEPTION(HttpStatus.BAD_REQUEST, "TEMP4001", "이거는 테스트"),

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // 멤버 관려 에러
    EMAIL_PW_NOT_MATCHED(HttpStatus.BAD_REQUEST, "EMAIL400", "이메일 또는 비밀번호가 올바르지 않습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST,"EMAIL401" ,"이미 동일한 이메일로 생성된 계정이 존재합니다."),
    EMAIL_VERIFICATION_IN_PROGRESS(HttpStatus.BAD_REQUEST, "EMAIL402", "이미 진행 중인 이메일 인증이 있습니다. 이메일을 확인해주세요."),
    EMAIL_CHANGE_SAME_AS_CURRENT(HttpStatus.BAD_REQUEST, "EMAIL403", "기존과 동일한 이메일로는 변경하실 수 없습니다."),
    GOOGLE_USER_CANNOT_CHANGE_EMAIL(HttpStatus.BAD_REQUEST,"EMAIL404","구글 로그인 유저는 이메일 변경을 하실 수 없습니다."),
    GOOGLE_USER_CANNOT_CHANGE_PW(HttpStatus.BAD_REQUEST,"EMAIL405","구글 로그인 유저는 비밀번호 변경을 하실 수 없습니다."),
    EMAIL_CODE_INVALID(HttpStatus.BAD_REQUEST, "EMAIL406", "유효하지 않은 이메일 인증 코드입니다."),
    EMAIL_CODE_EXPIRED(HttpStatus.BAD_REQUEST,"EMAIL407","만료된 이메일 인증 코드입니다. 이메일 인증을 다시 요청해주세요."),

    PROFILE_NOT_COMPLETED(HttpStatus.FORBIDDEN, "PROFILE403", "프로필 정보를 먼저 완성해주세요."),

    NICKNAME_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER400", "닉네임은 필수 입니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER401", "사용자를 찾을 수 없습니다."),
    MEMBER_NOT_ADMIN(HttpStatus.FORBIDDEN, "MEMBER402", "관리자 권한이 필요합니다."),
    ALREADY_LOGGED_OUT(HttpStatus.BAD_REQUEST, "MEMBER403", "이미 로그아웃된 사용자입니다."),
    WITHDRAWAL_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "MEMBER501", "계정 탈퇴 처리 중 오류가 발생했습니다."),

    // 비밀번호 관련 에러 추가
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "PASSWORD401", "유효하지 않은 비밀번호입니다."),
    PASSWORD_CURRENT_MISMATCH(HttpStatus.BAD_REQUEST, "PASSWORD402", "현재 비밀번호가 일치하지 않습니다."),
    PASSWORD_SAME_AS_CURRENT(HttpStatus.BAD_REQUEST, "PASSWORD403", "새 비밀번호는 현재 비밀번호와 달라야 합니다."),
    PASSWORD_RESET_CODE_INVALID(HttpStatus.BAD_REQUEST, "PASSWORD404", "유효하지 않은 비밀번호 재설정 코드입니다."),
    PASSWORD_RESET_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "PASSWORD405", "만료된 비밀번호 재설정 코드입니다. 비밀번호 재설정을 다시 요청해주세요."),

    // 냉장고 스킨 관련 에러
    DEFAULT_REFRIGERATOR_SKIN_NOT_FOUND(HttpStatus.NOT_FOUND, "SKIN401","기본 냉장고 스킨을 찾을 수 없습니다."),
    REFRIGERATOR_SKIN_NOT_FOUND(HttpStatus.NOT_FOUND, "SKIN402","냉장고 스킨을 찾을 수 없습니다."),
    REFRIGERATOR_SKIN_ALREADY_OWNED(HttpStatus.BAD_REQUEST, "SKIN403", "이미 구매한 냉장고 스킨입니다."),
    REFRIGERATOR_SKIN_NOT_OWNED(HttpStatus.BAD_REQUEST, "SKIN404", "구매하지 않은 냉장고 스킨입니다."),
    EQUIPPED_REFRIGERATOR_SKIN_NOT_OWNED(HttpStatus.NOT_FOUND, "SKIN405", "장착 중인 냉장고 스킨이 없습니다."),
    ALREADY_EQUIPPED(HttpStatus.BAD_REQUEST, "SKIN406", "이미 장착 중인 스킨입니다."),
    INSUFFICIENT_POINTS(HttpStatus.BAD_REQUEST, "POINT401", "포인트가 부족합니다."),

    //페이징 시 페이지 범위를 벗어났을 때
    PAGE_OUT_OF_RANGE(HttpStatus.BAD_REQUEST, "PAGE401","페이지 범위에 맞지 않는 페이지 값 입니다."),

    //계정 로그인 관련 에러
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTH401", "로그인이 필요한 서비스입니다."),

    //이미지
    IMAGE_FORMAT_BAD_REQUEST(HttpStatus.BAD_REQUEST,"IMAGE400","이미지 파일만 업로드할 수 있습니다."),
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "IMAGE501", "이미지 업로드에 실패했습니다. 다시 시도해주세요."),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "S3_400", "지원하지 않는 파일 형식입니다. (이미지 파일만 허용됩니다.)"),
    S3_FILE_DELETE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_500", "S3 파일 삭제에 실패했습니다."),
    S3_PRESIGNED_URL_GENERATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S3_501", "Presigned URL 생성에 실패했습니다."),

    //재료 관련 에러
    INGREDIENT_NOT_FOUND(HttpStatus.NOT_FOUND, "INGREDIENT401", "재료를 찾을 수 없습니다."),

    //레시피 관련 에러
    RECIPE_NOT_FOUND(HttpStatus.NOT_FOUND, "RECIPE401", "해당 레시피를 찾을 수 없습니다."),
    RECIPE_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "RECIPE402", "레시피 삭제 권한이 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
