package me.parade.lib_common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.LeadingMarginSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.appcompat.widget.AppCompatTextView;

import me.parade.lib_common.R;


/**
 * Created by parade岁月 on 2019/8/21 12:22
 * 组合TextView,分左中右  ，右侧的文字就是原生的text
 * 在代码中设置主要文本(最右侧)需要使用setContent(content)
 * 可设置从第二行往后每一行的缩进
 * 第一行中间的间距可用&#8195;代表一个字符，&#8194;代表半个字符的宽度
 * 比如表单项前加必读或者显示配置项的label和text
 */
public class CombineTextView extends AppCompatTextView {

    private static final String TAG = "CombineTextView";

    private String mPrefix;
    private int prefixSize;
    private int mPrefixColor = Color.RED;
    private String mMiddleText;
    private int mMiddleTextColor;
    private String mContent;
    private int mRestIntentSpace;
    private long mLastActionDownTime = 0;

    public CombineTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public CombineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CombineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        try (TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CombineTextView)) {
            mPrefix = array.getString(R.styleable.CombineTextView_prefix);
            prefixSize = array.getDimensionPixelSize(R.styleable.CombineTextView_prefix_text_size, (int) getTextSize());
            mPrefixColor = array.getInteger(R.styleable.CombineTextView_prefix_text_color, Color.RED);
            mMiddleText = array.getString(R.styleable.CombineTextView_middle_text);
            mMiddleTextColor = array.getColor(R.styleable.CombineTextView_middle_text_color, mPrefixColor);
            mContent = array.getString(R.styleable.CombineTextView_android_text);
            mRestIntentSpace = array.getInteger(R.styleable.CombineTextView_rest_intent_space, 0);
            if (null == mPrefix) mPrefix = "";
            if (null == mMiddleText) mMiddleText = "";
            if (null == mContent) mContent = "";
            setTextContent();
        }
    }


    private void setTextContent() {
        int start = 0, end = 0;
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(mPrefix);
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(mPrefixColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new AbsoluteSizeSpan(prefixSize), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        start = end;
        builder.append(mMiddleText);
        end = builder.length();
        builder.setSpan(new ForegroundColorSpan(mMiddleTextColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        start = end;
        builder.append(mContent);
        end = builder.length();
        builder.setSpan(new LeadingMarginSpan.Standard(0, (int) getTextSize() * mRestIntentSpace), 0, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        setText(builder);
    }

    public void setContent(String content) {
        if (null == content) {
            this.mContent = "";
        } else {
            this.mContent = content;
        }
        setTextContent();
    }

    public String getContent() {
        return this.mContent;
    }

    public void setPrefix(String prefix) {
        if (null == prefix) {
            this.mPrefix = "";
        } else {
            this.mPrefix = prefix;
        }
        setTextContent();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastActionDownTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_UP:
                long actionUpTime = System.currentTimeMillis();
                if (actionUpTime - mLastActionDownTime < ViewConfiguration.getLongPressTimeout()) {
                    performClick();
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
