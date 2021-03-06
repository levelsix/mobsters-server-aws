package com.lvl6.mobsters.info;

import java.io.Serializable;
import java.util.List;

//@Entity
//@Table(name="dialogue")
public class Dialogue implements Serializable { //extends BasePersistentObject{

	
	private static final long serialVersionUID = -8118069262824842364L;  
	
	
//	@ElementCollection(fetch=FetchType.EAGER)
//	@Column(name = "is_left_sides")
	List<Boolean> isLeftSides;
	
//	@ElementCollection(fetch=FetchType.EAGER)
//	@Column(name = "speakers")
	List<String> speakers;
	
//	@ElementCollection(fetch=FetchType.EAGER)
//	@Column(name = "speaker_images")
	List<String> speakerImages;
	
//	@ElementCollection(fetch=FetchType.EAGER)
//	@Column(name = "speaker_texts")
	List<String> speakerTexts;

	public Dialogue(){}
	public Dialogue(List<String> speakers, List<String> speakerImages, List<String> speakerTexts, List<Boolean> isLeftSides) {
		this.speakers = speakers;
		this.speakerTexts = speakerTexts;
		this.speakerImages = speakerImages;
		this.isLeftSides = isLeftSides;
	}

  public List<Boolean> getIsLeftSides() {
    return isLeftSides;
  }

  public List<String> getSpeakers() {
    return speakers;
  }

  public List<String> getSpeakerImages() {
    return speakerImages;
  }

	public List<String> getSpeakerTexts() {
		return speakerTexts;
	}

	@Override
	public String toString() {
		return "Dialogue [speakers=" + speakers + ", speakerTexts="
				+ speakerTexts + "]";
	}

}
