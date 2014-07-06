package opengl.lance.demo_11;

import org.lance.main.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class Constant_3 {

	public static final float DEGREE_SPAN = (float) (5.0 / 180.0f * Math.PI);// �����ÿ��ת���ĽǶ�
	public static final float MOVE_SPAN = 0.8f;// �����ÿ���ƶ���λ��
	public static final float UNIT_SIZE = 4f;// ÿ��ĵ�λ����
	public static final float DIRECTION_INI = 0.0f;// ��ʼ���߷��� ��Z�Ḻ��Ϊ0�ȣ���Y��������ת
	public static final float DISTANCE = 2.0f;// �����λ�þ���۲�Ŀ���ľ���

	public static final float LAND_HIGH_ADJUST = 2f;// ½�صĸ߶ȵ���ֵ
	public static final float WATER_HIGH_ADJUST = -0.2f;// ˮ��ĸ߶ȵ���ֵ
	public static final float LAND_HIGHEST = 44f;// ½�����߲�

	public static float[][] yArray;// ½����ÿ������ĸ߶�����
	public static int COLS;// ½������
	public static int ROWS;// ½������
	// �������ʼXYZ����
	public static float CAMERA_INI_X = 0;
	// public static float CAMERA_INI_Y=10;//ƽ��
	public static float CAMERA_INI_Y = 55;// ���
	public static float CAMERA_INI_Z = 0;

	public static void initConstant(Resources r) {
		// �ӻҶ�ͼƬ�м���½����ÿ������ĸ߶�
		yArray = loadLandforms(r);
		// ���������С����½�ص�����������
		COLS = yArray[0].length - 1;
		ROWS = yArray.length - 1;
	}

	// �ӻҶ�ͼƬ�м���½����ÿ������ĸ߶�----���ػҶ�ͼ---�Ҷ�ͼ��һ�ֵ���ͼ
	private static float[][] loadLandforms(Resources resources) {
		Bitmap bt = BitmapFactory.decodeResource(resources, R.drawable.land);
		// ����λͼ�Ŀ��
		int colsPlusOne = bt.getWidth();
		// λͼ�ĸ߶�;
		int rowsPlusOne = bt.getHeight();
		// ����λͼ�Ŀ�ߴ���һ������---����ÿһ�����ص����ɫֵ
		float[][] result = new float[rowsPlusOne][colsPlusOne];
		for (int i = 0; i < rowsPlusOne; i++) {
			for (int j = 0; j < colsPlusOne; j++) {
				// ����λͼ��ָ�������������ɫֵ
				int color = bt.getPixel(j, i);
				// ������ɫֵ�������������ԭɫ
				int r = Color.red(color);
				int g = Color.green(color);
				int b = Color.blue(color);
				int h = (r + g + b) / 3;// ����ƽ����ɫ
				// �õ���ǰ��---����ƽ��colorֵ---ע��������һ��Ҫע��˳��
				result[i][j] = h * LAND_HIGHEST / 255 - LAND_HIGH_ADJUST;
			}
		}
		return result;
	}
}
