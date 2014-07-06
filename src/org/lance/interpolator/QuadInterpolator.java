package org.lance.interpolator;

import android.view.animation.Interpolator;

/**
 * 
 * @author lance
 * 
 */
public class QuadInterpolator implements Interpolator {

	private int type;

	public QuadInterpolator(int type) {
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
		return t * t;
	}

	private float out(float t) {
		return -(t) * (t - 2);
	}

	private float inout(float t) {
		t *= 2;
		if (t < 1) {
			return 0.5f * t * t;
		} else {
			return -0.5f * ((--t) * (t - 2) - 1);
		}
	}
}
