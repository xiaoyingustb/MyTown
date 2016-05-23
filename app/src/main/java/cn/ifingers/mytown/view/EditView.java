package cn.ifingers.mytown.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

public class EditView extends EditText {
	private Drawable drawableRight;

	public EditView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(getCompoundDrawables()[2] != null){
			drawableRight = getCompoundDrawables()[2];
			drawableRight.setVisible(false, false);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int eventType = event.getAction();
		if (eventType == MotionEvent.ACTION_DOWN) {
			if (drawableRight == null) {
				return false;
			}
			if (event.getX() > getWidth() - drawableRight.getIntrinsicWidth()
					- getPaddingRight()) {
				setText("");
				invalidate();
			}
		}
		return super.onTouchEvent(event);
	}

}
