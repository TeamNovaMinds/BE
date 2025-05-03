package novaminds.gradproj.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.common.BaseEntity;
import novaminds.gradproj.domain.enums.RoomParticipantStatus;

@Entity
@Getter
@NoArgsConstructor
public class RoomParticipant extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "login_id", nullable = false)
	private User user;

	@ManyToOne
	@JoinColumn(name = "meeting_room_id", nullable = false)
	private MeetingRoom meetingRoom;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RoomParticipantStatus status;

	@Column
	private LocalDateTime joinedAt;

	@Lob
	private String availableTimesJson;


}

