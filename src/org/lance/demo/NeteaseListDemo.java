package org.lance.demo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;

import org.lance.adapters.NetPushAdapter;
import org.lance.entity.Project;
import org.lance.main.R;
import org.lance.widget.PushableListView;

public class NeteaseListDemo extends Activity {
	private PushableListView netList;
	private NetPushAdapter npAdapter;
	private String[] sections = { "羽毛球", "乒乓球", "竞技体操", "游泳", "射击", "长跑", "跳水",
			"篮球", "女足" };
	private List<Project> pros = new ArrayList<Project>();

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.neteast_main);
		netList = (PushableListView) findViewById(R.id.net_list);

		init();
	}

	private void init() {
		for (int i = 0; i < sections.length; i++) {
			if (i % 2 == 0) {
				Project pro0 = new Project();
				pro0.setGroupName(sections[i]);
				pro0.setDesc("男子400米自由泳");
				pros.add(pro0);
				Project pro1 = new Project();
				pro1.setGroupName(sections[i]);
				pro1.setDesc("男子乒乓球单打");
				pros.add(pro1);
				Project pro3 = new Project();
				pro3.setGroupName(sections[i]);
				pro3.setDesc("女子跆拳道单打");
				pros.add(pro3);
			} else if (i % 3 == 0) {
				Project pro4 = new Project();
				pro4.setGroupName(sections[i]);
				pro4.setDesc("女子400米长跑");
				pros.add(pro4);
			} else if (i % 5 == 0) {
				Project pro = new Project();
				pro.setGroupName(sections[i]);
				pro.setDesc("男子仰泳");
				pros.add(pro);
				Project pro1 = new Project();
				pro1.setGroupName(sections[i]);
				pro1.setDesc("男子散打");
				pros.add(pro1);
			}
			Project pro5 = new Project();
			pro5.setGroupName(sections[i]);
			pro5.setDesc("女子600米自由泳");
			pros.add(pro5);
			Project pro6 = new Project();
			pro6.setGroupName(sections[i]);
			pro6.setDesc("女子排球");
			pros.add(pro6);
		}
		npAdapter = new NetPushAdapter(this, sections, pros);
		netList.setTitleView(LayoutInflater.from(this).inflate(
				R.layout.net_title, netList, false));
		netList.setAdapter(npAdapter);
		netList.setOnScrollListener(npAdapter);
		netList.setOnPushListener(npAdapter);
	}

}
