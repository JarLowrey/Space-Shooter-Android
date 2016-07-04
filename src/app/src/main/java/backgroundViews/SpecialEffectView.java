package backgroundViews;

import com.jtronlabs.space_shooter.GameLoop;

import android.widget.RelativeLayout;

import java.util.ArrayList;

import parents.MovingView;

public abstract class SpecialEffectView extends MovingView {


	private static ArrayList<SpecialEffectView> specialEffectViewPool = new ArrayList<SpecialEffectView>();

	public SpecialEffectView(float xInitialPosition,float yInitialPosition,RelativeLayout layout, float movingSpeedY,
			float movingSpeedX, int width, int height, int imageId) {
		super(xInitialPosition,yInitialPosition,layout, movingSpeedY, movingSpeedX, width, height, imageId);

		initSpecialEffect();
	}

	public void unRemoveSpecialEffectView(float xInitialPosition,float yInitialPosition,RelativeLayout layout, float movingSpeedY,
						 float movingSpeedX, int width, int height, int imageId){
		super.unRemove(xInitialPosition,yInitialPosition,layout,movingSpeedY,movingSpeedX,width,height,imageId);

		initSpecialEffect();
	}

	public static SpecialEffectView getEffect(RelativeLayout layout, int imageId,Class effectClass,
									  MovingView parent,long[] vibrationPattern){
		for(SpecialEffectView sev : specialEffectViewPool){
			if(sev.isRemoved() && effectClass.equals(sev.getClass())){

				if(sev.getClass().equals(ExplosionView.class)){
					((ExplosionView)sev).unremoveExplosion(layout,parent,imageId,vibrationPattern);
				}else if(effectClass.equals(ExplosionView.class)){
					((StarView)sev).unRemoveStarView(layout);
				}

				return sev;
			}
		}

		if(effectClass.equals(StarView.class)){
			return new StarView(layout);
		}else if(effectClass.equals(ExplosionView.class)){
			return new ExplosionView(layout,parent,imageId,vibrationPattern);
		}else{
			return null;
		}
	}

	private void initSpecialEffect(){
		GameLoop.specialEffects.add(this);
	}

}
