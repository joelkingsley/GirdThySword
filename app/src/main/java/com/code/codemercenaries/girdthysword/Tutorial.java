package com.code.codemercenaries.girdthysword;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;

import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.SimpleTarget;

/**
 * Created by Joel Kingsley on 01-01-2018.
 */

public class Tutorial {
    Activity activity;
    View view;
    Point point;
    float radius;
    String title;
    String desc;

    public Tutorial(Activity activity, View view, float radius, String title, String desc) {
        this.activity = activity;
        this.view = view;
        this.radius = radius;
        this.title = title;
        this.desc = desc;
        this.point = null;
    }

    public Tutorial(Activity activity, Point point, float radius, String title, String desc) {
        this.activity = activity;
        this.point = point;
        this.radius = radius;
        this.title = title;
        this.desc = desc;
        this.view = null;
    }

    public SimpleTarget generateTarget() {
        if (point == null) {
            SimpleTarget target = new SimpleTarget.Builder(activity)
                    .setPoint(view) // position of the Target. setPoint(Point point), setPoint(View view) will work too.
                    .setRadius(radius) // radius of the Target
                    .setTitle(title) // title
                    .setDescription(desc) // description
                    .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                        @Override
                        public void onStarted(SimpleTarget target) {
                            // do something
                        }

                        @Override
                        public void onEnded(SimpleTarget target) {
                            // do something
                        }
                    })
                    .build();
            return target;
        } else {
            SimpleTarget target = new SimpleTarget.Builder(activity)
                    .setPoint(point.x, point.y) // position of the Target. setPoint(Point point), setPoint(View view) will work too.
                    .setRadius(radius) // radius of the Target
                    .setTitle(title) // title
                    .setDescription(desc) // description
                    .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                        @Override
                        public void onStarted(SimpleTarget target) {
                            // do something
                        }

                        @Override
                        public void onEnded(SimpleTarget target) {
                            // do something
                        }
                    })
                    .build();
            return target;
        }
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
