package novaminds.gradproj.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@ManyToOne(optional = false)
	@JoinColumn(name = "from_user", nullable = false)
	private User fromUser;

	@ManyToOne(optional = false)
	@JoinColumn(name = "to_user", nullable = false)
	private User toUser;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FriendshipStatus status;


}
