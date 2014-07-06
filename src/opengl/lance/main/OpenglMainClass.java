package opengl.lance.main;

import java.util.ArrayList;
import java.util.List;

import opengl.lance.demo_10.Sample10_1;
import opengl.lance.demo_10.Sample10_2;
import opengl.lance.demo_11.Sample11_1;
import opengl.lance.demo_11.Sample11_2;
import opengl.lance.demo_11.Sample11_3;
import opengl.lance.demo_11.Sample11_4;
import opengl.lance.demo_13.Sample13_1;
import opengl.lance.demo_13.Sample13_2;
import opengl.lance.demo_13.Sample13_3;
import opengl.lance.demo_13.Sample13_4;
import opengl.lance.demo_13.Sample13_5;
import opengl.lance.demo_14.Sample14_1;
import opengl.lance.demo_15.Sample15_2;
import opengl.lance.demo_15.Sample15_3;
import opengl.lance.demo_15.Sample15_4;
import opengl.lance.demo_15.Sample15_5;
import opengl.lance.demo_15.Sample15_6;
import opengl.lance.demo_15.Sample15_7;
import opengl.lance.demo_15.Sample15_8;
import opengl.lance.demo_4.HexDemo;
import opengl.lance.demo_4.OtherDemo;
import opengl.lance.demo_4.PairDemo;
import opengl.lance.demo_5.MyActivity_3;
import opengl.lance.demo_5.MyActivity_4;
import opengl.lance.demo_5.MyActivity_5;
import opengl.lance.demo_5.MyActivity_6;
import opengl.lance.demo_5.OtherDemo_1;
import opengl.lance.demo_5.OtherDemo_2;
import opengl.lance.demo_6.Sample6_1;
import opengl.lance.demo_6.Sample6_2;
import opengl.lance.demo_6.Sample6_3;
import opengl.lance.demo_6.Sample6_4;
import opengl.lance.demo_6.Sample6_5;
import opengl.lance.demo_7.Activity_GL_Cirque_1;
import opengl.lance.demo_7.Activity_GL_Cirque_2;
import opengl.lance.demo_7.Activity_GL_Cylinder_1;
import opengl.lance.demo_7.Activity_GL_Cylinder_2;
import opengl.lance.demo_7.Activity_GL_HelicoidSurface_1;
import opengl.lance.demo_7.Activity_GL_HelicoidSurface_2;
import opengl.lance.demo_7.Activity_GL_Hyperboloid_1;
import opengl.lance.demo_7.Activity_GL_Hyperboloid_2;
import opengl.lance.demo_7.Activity_GL_Paraboloid_1;
import opengl.lance.demo_7.Activity_GL_Paraboloid_2;
import opengl.lance.demo_7.Activity_GL_Taper_1;
import opengl.lance.demo_7.Activity_GL_Taper_2;
import opengl.lance.demo_8.Sample8_1;
import opengl.lance.demo_8.Sample8_2;
import opengl.lance.demo_8.Sample8_3;
import opengl.lance.demo_8.Sample8_4;
import opengl.lance.demo_9.GL_Demo;
import org.lance.adapters.RootAdapter;
import org.lance.main.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * opengl列表界面~地图搜索和天空穹在高分辨率下测试效果有差异
 * 
 * @author Administrator
 * 
 */
public class OpenglMainClass extends Activity {
	private ListView list;
	private RootAdapter adapter;
	private String[] srr = { "点与线", "三角形对", "正交和透视", "光照效果", "多盏灯效果", "光源强度变换",
			"运动光源", "面法向量",
			"平均法向量",
			// 纹理技术
			"纹理映射三角形", "纹理映射球体", "地月系", "纹理拉伸和过滤",
			"视角设置",
			// 基本图形绘制
			"纹理圆柱体", "线形圆柱体", "纹理锥体", "线形锥体", "纹理圆环", "线形圆环", "纹理抛物面", "线形抛物面",
			"纹理双曲面", "线形双曲面", "纹理螺旋体", "线形螺旋体",
			// 坐标转换
			"坐标缩放转换", "坐标平移转换", "坐标旋转转换", "坐标复合转换",
			// 摄像机与雾特效
			"雾特效",
			// 混合技术
			"源因子和目标因子", "地月系混合案例",
			// 3D高级技术
			"标志板(需要实体键)", "飘扬的旗帜", "山地生成技术", "镜像技术",
			// 数学与物理知识
			"重力系统", "碰撞检测(无损)", "碰撞检测(有损)", "粒子系统(单纯颜色)", "粒子系统(标志板应用)",
			// AI原理
			"地图搜索(AI引擎)",
			// 开发秘笈
			"多键技术(需要实体键)", "虚拟键盘", "AABB边界(碰撞检测)", "球与AABB(碰撞检测)", "穿透效应",
			"拾取技术", "天空穹" };
	private Class[] crr = { OtherDemo.class,// 点与线
			PairDemo.class,// 三角形对
			HexDemo.class,// 正交和透视
			OtherDemo_1.class,// 光照效果
			OtherDemo_2.class,// 多盏灯效果
			MyActivity_3.class,// 光源强度变换
			MyActivity_4.class,// 运动光源
			MyActivity_5.class,// 面法向量
			MyActivity_6.class,// 平均法向量

			Sample6_1.class,// 纹理映射三角形
			Sample6_2.class,// 纹理映射球体
			Sample6_3.class,// 地月系
			Sample6_4.class,// 纹理拉伸和过滤
			Sample6_5.class,// 视角设置
			// 基本图形绘制
			Activity_GL_Cylinder_1.class,// 纹理圆柱体
			Activity_GL_Cylinder_2.class,// 线形圆柱体
			Activity_GL_Taper_1.class,// 纹理锥体
			Activity_GL_Taper_2.class,// 线形锥体
			Activity_GL_Cirque_1.class,// 纹理圆环
			Activity_GL_Cirque_2.class,// 线形圆环
			Activity_GL_Paraboloid_1.class,// 纹理抛物面
			Activity_GL_Paraboloid_2.class,// 线形抛物面
			Activity_GL_Hyperboloid_1.class,// 纹理双曲面
			Activity_GL_Hyperboloid_2.class,// 线形双曲面
			Activity_GL_HelicoidSurface_1.class,// 纹理螺旋体
			Activity_GL_HelicoidSurface_2.class,// 线形螺旋体
			// 坐标变换
			Sample8_1.class,// 坐标缩放转换
			Sample8_2.class,// 坐标平移转换
			Sample8_3.class,// 坐标旋转转换
			Sample8_4.class,// 坐标复合转换
			// 摄像机与雾特效
			GL_Demo.class,// 雾特效
			// 混合技术
			Sample10_1.class,// 源因子和目标因子
			Sample10_2.class,// 地月系混合案例
			// 标志板~帧动画技术~山地生成~镜像技术
			Sample11_1.class,// 标志板(需要实体键)
			Sample11_2.class,// 飘扬的旗帜
			Sample11_3.class,// 山地生成
			Sample11_4.class,// 镜像技术
			// 游戏中的数学与物理
			Sample13_1.class,// 重力系统
			Sample13_2.class,// 碰撞检测(能量无损)
			Sample13_3.class,// 碰撞检测(能量有损)
			Sample13_4.class,// 粒子系统(单纯颜色粒子)
			Sample13_5.class,// 粒子系统(标志板技术)
			// AI引擎~地图搜索技术
			Sample14_1.class,// 地图搜索~高分辨率手机上测试效果不尽相同
			// 地图设计器~多键技术~虚拟键盘~智能体~AABB边界~穿透效应~拾取技术~天空穹
			Sample15_2.class,// 多键技术(需要实体键)
			Sample15_3.class,// 虚拟键盘
			Sample15_4.class,// AABB边界
			Sample15_5.class,// 球与AABB碰撞
			Sample15_6.class,// 穿透效应
			Sample15_7.class,// 拾取技术
			Sample15_8.class,// 天空穹~高分辨率手机上测试效果不尽相同
	};

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.ck_main);
		list = (ListView) findViewById(R.id.main_listview);
		setAdapter();
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(OpenglMainClass.this, crr[position]));
			}
		});
	}

	private void setAdapter() {
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < srr.length; i++) {
			data.add(srr[i]);
		}
		adapter = new RootAdapter(this, data);
		list.setAdapter(adapter);
	}
}
