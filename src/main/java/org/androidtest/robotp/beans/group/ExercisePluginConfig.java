package org.androidtest.robotp.beans.group;

import java.io.Serializable;

/**
 * ExercisePluginConfig java bean
 *
 * @Author: Vince蔡培培
 */
public class ExercisePluginConfig implements Serializable {

	private boolean recallSelfClockin;
	private boolean clockinOnceADay;

	public boolean getRecallSelfClockin() {
		return recallSelfClockin;
	}

	public void setRecallSelfClockin(boolean recallSelfClockin) {
		this.recallSelfClockin = recallSelfClockin;
	}

	public boolean getClockinOnceADay() {
		return clockinOnceADay;
	}

	public void setClockinOnceADay(boolean clockinOnceADay) {
		this.clockinOnceADay = clockinOnceADay;
	}

	@Override
	public String toString() {
		return "ExercisePluginConfig{" + "recallSelfClockin="
				+ recallSelfClockin + ", clockinOnceADay=" + clockinOnceADay
				+ '}';
	}
}