package novaminds.gradproj.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.common.BaseEntity;
import novaminds.gradproj.domain.enums.FriendshipStatus;

@Entity
@Getter
@NoArgsConstructor
public class Friendship extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "from_user", nullable = false)
	private User fromUser;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "to_user", nullable = false)
	private User toUser;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FriendshipStatus status;


}
