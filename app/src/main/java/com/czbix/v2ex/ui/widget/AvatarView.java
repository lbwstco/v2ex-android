package com.czbix.v2ex.ui.widget;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.czbix.v2ex.R;
import com.czbix.v2ex.model.Avatar;
import com.czbix.v2ex.model.Member;

public class AvatarView extends AppCompatImageView {
    private int mRealSize;
    private boolean mHasRealSize;

    public AvatarView(Context context) {
        this(context, null);
    }

    public AvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int getRealSize() {
        if (!mHasRealSize) {
            mRealSize = getLayoutParams().width - getPaddingTop() * 2 ;
            mHasRealSize = true;
        }

        return mRealSize;
    }

    public void setAvatar(Avatar avatar) {
        final RequestManager glide = Glide.with(getContext());
        if (avatar == null) {
            glide.clear(this);
            return;
        }

        final int size = getRealSize();
        final String url = avatar.getUrlByPx(size);
        glide.load(url).placeholder(R.drawable.avatar_default)
                .override(size, size).fitCenter().transition(DrawableTransitionOptions.withCrossFade()).into(this);
    }

    public interface OnAvatarActionListener {
        void onMemberClick(Member member);
    }
}
