package novaminds.gradproj.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.common.BaseEntity;
import novaminds.gradproj.domain.enums.Role;
import novaminds.gradproj.domain.enums.SocialType;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseEntity {

	@Id
	@Column(name = "login_id", nullable = false, unique = true)
	private String loginId;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	private String profileImage;

	@Column(name = "provider_id")
	private String providerId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Enumerated(EnumType.STRING)
	@Column(name = "social_type", nullable = false)
	private SocialType socialType;
}
