package novaminds.gradproj.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
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

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "login_id", nullable = false)
	private User user;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "meeting_room_id", nullable = false)
	private MeetingRoom meetingRoom;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RoomParticipantStatus status;

	@Column
	private LocalDateTime joinedAt;

	@Lob
	private String availableTimesJson;

	@OneToMany(mappedBy = "roomParticipant", cascade = CascadeType.PERSIST)
	private List<TimeVote> votes = new ArrayList<>();

}

