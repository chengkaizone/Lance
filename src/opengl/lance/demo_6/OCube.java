package opengl.lance.demo_6;

import javax.microedition.khronos.opengles.GL10;

import static opengl.lance.demo_6.OContant.*;

public class OCube {
	OColorRect rect = new OColorRect(SCALE, SCALE);

	public void drawSelf(GL10 gl) {
		gl.glPushMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0, 0, UNIT_SIZE * SCALE);
		rect.drawSelf(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0, 0, -UNIT_SIZE * SCALE);
		gl.glRotatef(180, 0, 1, 0);
		rect.drawSelf(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0, UNIT_SIZE * SCALE, 0);
		gl.glRotatef(-90, 1, 0, 0);
		rect.drawSelf(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(0, -UNIT_SIZE * SCALE, 0);
		gl.glRotatef(90, 1, 0, 0);
		rect.drawSelf(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(UNIT_SIZE * SCALE, 0, 0);
		gl.glRotatef(-90, 1, 0, 0);
		gl.glRotatef(90, 0, 1, 0);
		rect.drawSelf(gl);
		gl.glPopMatrix();

		gl.glPushMatrix();
		gl.glTranslatef(-UNIT_SIZE * SCALE, 0, 0);
		gl.glRotatef(90, 1, 0, 0);
		gl.glRotatef(-90, 0, 1, 0);
		rect.drawSelf(gl);
		gl.glPopMatrix();

		gl.glPopMatrix();
	}
}
