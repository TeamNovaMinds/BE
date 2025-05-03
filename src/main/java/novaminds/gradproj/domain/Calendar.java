package novaminds.gradproj.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import novaminds.gradproj.domain.common.BaseEntity;
import novaminds.gradproj.domain.enums.CalendarTheme;
import novaminds.gradproj.domain.enums.CalendarVisibilityLevel;

@Entity
@Getter
@NoArgsConstructor
public class Calendar extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "login_id", nullable = false)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CalendarTheme theme;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private CalendarVisibilityLevel visibilityLevel;

	@OneToMany(mappedBy = "calendar", cascade = CascadeType.ALL)
	private List<Events> events = new ArrayList<>();


}

