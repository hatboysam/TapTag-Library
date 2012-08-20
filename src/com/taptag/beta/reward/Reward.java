package com.taptag.beta.reward;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Reward implements Serializable, Comparable<Reward> {
	
	private Integer id;
	private String name;
	private String description;
	private int progress;
	private int total;
	private boolean repeats;
	private Date end_date;

	public Reward() {
		
	}
	
	public Reward(String title, String description, int progress, int total) {
		this.name = title;
		this.description = description;
		this.progress = progress;
		this.total = total;
		this.repeats = false;
	}
	
	public Double score() {
		return ((double) (progress / total));
	}
	
	/**
	 * Returns progress in the form "{progress}/{total}"
	 * @return
	 */
	public String getProgressString() {
		return (Integer.toString(getProgressBounded()) + "/" + Integer.toString(total));
	}
	
	/**
	 * Determines if a reward ends before today
	 * @return
	 */
	public boolean isExpired() {
		int compare = end_date.compareTo(new Date());
		return (compare < 0);
	}
	
	//============================================================
	//====================GETTERS AND SETTERS=====================
	//============================================================

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getProgress() {
		return progress;
	}
	
	public int getProgressBounded() {
		return Math.min(progress, total);
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}
	
	public boolean getRepeats() {
		return repeats;
	}

	public void setRepeats(boolean repeats) {
		this.repeats = repeats;
	}

	public Date getEnd_date() {
		return end_date;
	}

	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}

	@Override
	public int compareTo(Reward another) {
		return (another.score().compareTo(this.score()));
	}
	
}
