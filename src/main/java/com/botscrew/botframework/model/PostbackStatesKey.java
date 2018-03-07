package com.botscrew.botframework.model;

public class PostbackStatesKey {
	private String postback;
	private String state;

	public PostbackStatesKey(String postback, String state) {
		super();
		this.postback = postback;
		this.state = state;
	}

	public String getPostback() {
		return postback;
	}

	public void setPostback(String postback) {
		this.postback = postback;
	}

	public String getState() {
		return state;
	}

	public void setState(String states) {
		this.state = states;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((postback == null) ? 0 : postback.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PostbackStatesKey other = (PostbackStatesKey) obj;
		if (postback == null) {
			if (other.postback != null) {
				return false;
			}
		} else if (!postback.equals(other.postback)) {
			return false;
		}
		return state.equals(other.state);
	}

}
