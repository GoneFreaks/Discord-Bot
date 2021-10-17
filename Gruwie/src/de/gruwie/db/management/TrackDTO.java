package de.gruwie.db.management;

public class TrackDTO {

	private int id;
	private String url;
	private int genre1;
	private int genre2;
	private int genre3;
	
	public TrackDTO(int id, String url, int genre1, int genre2, int genre3) {
		this.id = id;
		this.url = url;
		this.genre1 = genre1;
		this.genre2 = genre2;
		this.genre3 = genre3;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getGenre1() {
		return genre1;
	}

	public int getGenre2() {
		return genre2;
	}

	public int getGenre3() {
		return genre3;
	}

	public void setGenre(int genre, int index) {
		switch (index) {
			case 0: {
				this.genre1 = genre;
				break;
			}
			case 1: {
				this.genre2 = genre;
				break;
			}
			case 2: {
				this.genre3 = genre;
				break;
			}
		}
	}
	
	@Override
	public String toString() {
		return "Id: " + id + "\nurl: " + url + "\ngenre1: " + genre1 + "\ngenre2: " + genre2 + "\ngenre3: " + genre3;
	}
	
}
