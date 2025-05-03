package novaminds.gradproj.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.common.BaseEntity;

@Entity
@Getter
@NoArgsConstructor
public class MeetingRoom extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "login_id", nullable = false)
	private User creator;

	@Column(nullable = false)
	private String roomName;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private String inviteCode;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@Lob
	private String suggestedTimesJson;

	@Lob
	private String availabilityCalendarJson;

	private LocalDateTime finalizedTime;

	@OneToMany(mappedBy = "meetingRoom", cascade = CascadeType.ALL)
	private List<RoomParticipant> participants = new ArrayList<>();
}

