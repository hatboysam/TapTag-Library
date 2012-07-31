package com.taptag.beta.reward;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Reward implements Serializable, Comparable<Reward> {

	private Integer id;
	private String name;
	private String description;
	private int progress;
	private int total;

	public Reward() {
		
	}
	
	public Reward(String title, int progress, int total) {
		this.name = title;
		this.progress = progress;
		this.total = total;
	}
	
	public Double score() {
		return ((double) (progress / total));
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

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	@Override
	public int compareTo(Reward another) {
		return (another.score().compareTo(this.score()));
	}
	
}
