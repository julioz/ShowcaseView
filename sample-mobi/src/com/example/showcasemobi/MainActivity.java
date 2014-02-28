package com.example.showcasemobi;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.espian.showcaseview.ShowcaseView;
import com.espian.showcaseview.ShowcaseView.OffsetOrientation;
import com.espian.showcaseview.ShowcaseView.OnTargetChangeListener;
import com.espian.showcaseview.drawing.TextDrawer;
import com.espian.showcaseview.targets.ActionItemTarget;
import com.espian.showcaseview.targets.ActionViewTarget;
import com.espian.showcaseview.targets.ViewTarget;

public class MainActivity extends Activity {

	private ShowcaseView showcaseView;
	private int counter = 0;
	
	private LinearLayout llNewCharge;
	private ImageView btnIcon;
	private TextView tvNewCharge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		llNewCharge = (LinearLayout) findViewById(R.id.llnewcharge);
		tvNewCharge = (TextView) findViewById(R.id.tvnewcharge);
		btnIcon = (ImageView) findViewById(R.id.iv_btnicon);
		
		final ViewTarget targetLayout = new ViewTarget(llNewCharge);
		final ViewTarget targetIcon = new ViewTarget(btnIcon);
		final ViewTarget targetTextView = new ViewTarget(tvNewCharge);
		
		ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();
		co.blockAll = true;
		
		showcaseView = ShowcaseView.insertShowcaseView(targetLayout, this, "Nova Recarga", "Acesse a área Nova Recarga para recarregar números próprios, de parentes ou amigos.", targetTextView, OffsetOrientation.HORIZONTAL, co);
		showcaseView.setButtonText("Ok, entendi!");
		showcaseView.setTextTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf"));
		
		RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lps.addRule(RelativeLayout.CENTER_HORIZONTAL);
		int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
		lps.setMargins(margin, margin, margin, margin);
		showcaseView.setButtonLayoutParams(lps);
		
		showcaseView.setButtonBackground(getResources().getDrawable(R.drawable.btn_default_normal_holo_light));
		showcaseView.overrideButtonClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (counter) {
                    case 0:
                        showcaseView.setTextPosition(TextDrawer.POSITION_BOTTOM);
                        showcaseView.setText("Nova Recarga", "Clique no item Recarregar outro número para recarregar números não cadastrados.");
                        showcaseView.setOnTargetChangeListener(new OnTargetChangeListener() {
							@Override
							public void onMoveCompleted() {
								showcaseView.setShowcaseRadius(25);
							}
						});
                        showcaseView.setShowcase(targetIcon, false);
                        break;
                    case 1:
                        showcaseView.setText("Menu", "Agora você pode acessar o menu para gerenciar seus cartões de crédito, números cadastrados e recargas agendadas!");
                        showcaseView.setOnTargetChangeListener(new OnTargetChangeListener() {
							@Override
							public void onMoveCompleted() {
								showcaseView.setShowcaseRadius(35);
							}
						});
                        ActionViewTarget target = new ActionViewTarget(MainActivity.this, ActionViewTarget.Type.OVERFLOW);
                        showcaseView.setShowcase(target, true);
                    	break;
                    case 2:
                        showcaseView.hide();
                        break;
                }
                counter++;
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
