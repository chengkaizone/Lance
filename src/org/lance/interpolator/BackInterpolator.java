package org.lance.interpolator;

import android.view.animation.Interpolator;

/**
 * 
 * @author lance
 * 
 */
public class BackInterpolator implements Interpolator {

	private int type;
	private float overshot;

	public BackInterpolator(int type, float overshot) {
		this.type = type;
		this.overshot = overshot;
	}

	public float getInterpolation(float t) {
		if (type == EasingType.IN) {
			return in(t, overshot);
		} else if (type == EasingType.OUT) {
			return out(t, overshot);
		} else if (type == EasingType.INOUT) {
			return inout(t, overshot);
		}
		return 0;
	}

	private float in(float t, float o) {
		if (o == 0) {
			o = 1.70158f;
		}
		return t * t * ((o + 1) * t - o);
	}

	private float out(float t, float o) {
		if (o == 0) {
			o = 1.70158f;
		}
		return ((t -= 1) * t * ((o + 1) * t + o) + 1);
	}

	private float inout(float t, float o) {
		if (o == 0) {
			o = 1.70158f;
		}
		t *= 2;
		if (t < 1) {
			return 0.5f * (t * t * (((o *= (1.525)) + 1) * t - o));
		} else {
			return 0.5f * ((t -= 2) * t * (((o *= (1.525)) + 1) * t + o) + 2);
		}
	}
}
