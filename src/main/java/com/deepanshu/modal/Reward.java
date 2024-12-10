package com.deepanshu.modal;

import com.deepanshu.user.domain.RewardStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "rewards")
public class Reward {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	private int points;

	@Column(name = "credit_date")
	private LocalDateTime creditDate;

	@Column(name = "earned_points")
	private int earnedPoints;

	@Column(name = "used_points")
	private int usedPoints;

	@Column(name = "redeemed")
	private boolean redeemed;

	@Enumerated(EnumType.STRING)
	private RewardStatus status;

	@ElementCollection
	@CollectionTable(name = "redeemed_points_history", joinColumns = @JoinColumn(name = "reward_id"))
	@Column(name = "redeemed_points")
	private List<Integer> redeemedPointsHistory;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public LocalDateTime getCreditDate() {
		return creditDate;
	}

	public void setCreditDate(LocalDateTime creditDate) {
		this.creditDate = creditDate;
	}

	public int getEarnedPoints() {
		return earnedPoints;
	}

	public void setEarnedPoints(int earnedPoints) {
		this.earnedPoints = earnedPoints;
	}

	public int getUsedPoints() {
		return usedPoints;
	}

	public void setUsedPoints(int usedPoints) {
		this.usedPoints = usedPoints;
	}

	public boolean isRedeemed() {
		return redeemed;
	}

	public void setRedeemed(boolean redeemed) {
		this.redeemed = redeemed;
	}

	public RewardStatus getStatus() {
		return status;
	}

	public void setStatus(RewardStatus status) {
		this.status = status;
	}

	public List<Integer> getRedeemedPointsHistory() {
		return redeemedPointsHistory;
	}

	public void setRedeemedPointsHistory(List<Integer> redeemedPointsHistory) {
		this.redeemedPointsHistory = redeemedPointsHistory;
	}

	public Reward(Long id, User user, int points) {
		this.id = id;
		this.user = user;
		this.points = points;
		this.creditDate = LocalDateTime.now();
		this.redeemed = false;
		this.status = RewardStatus.Earned;
	}

	public Reward() {
		this.creditDate = LocalDateTime.now();
		this.redeemed = false;
		this.status = RewardStatus.Earned;
	}

}
