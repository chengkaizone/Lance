package org.lance.interpolator;

import android.view.animation.Interpolator;

/**
 * 
 * @author lance
 * 
 */
public class QuartInterpolator implements Interpolator {

	private int type;

	public QuartInterpolator(int type) {
		this.type = type;
	}

	public float getInterpolation(float t) {
		if (type == EasingType.IN) {
			return in(t);
		} else if (type == EasingType.OUT) {
			return out(t);
		} else if (type == EasingType.INOUT) {
			return inout(t);
		}
		return 0;
	}

	private float in(float t) {
		return t * t * t * t;
	}

	private float out(float t) {
		return -((t -= 1) * t * t * t - 1);
	}

	private float inout(float t) {
		t *= 2;
		if (t < 1) {
			return 0.5f * t * t * t * t;
		} else {
			return -0.5f * ((t -= 2) * t * t * t - 2);
		}
	}
}
