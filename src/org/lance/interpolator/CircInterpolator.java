package org.lance.interpolator;

import android.view.animation.Interpolator;

/**
 * 
 * @author lance
 * 
 */
public class CircInterpolator implements Interpolator {

	private int type;

	public CircInterpolator(int type) {
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
		return (float) -(Math.sqrt(1 - t * t) - 1);
	}

	private float out(float t) {
		return (float) Math.sqrt(1 - (t -= 1) * t);
	}

	private float inout(float t) {
		t *= 2;
		if (t < 1) {
			return (float) (-0.5f * (Math.sqrt(1 - t * t) - 1));
		} else {
			return (float) (0.5f * (Math.sqrt(1 - (t -= 2) * t) + 1));
		}
	}
}
