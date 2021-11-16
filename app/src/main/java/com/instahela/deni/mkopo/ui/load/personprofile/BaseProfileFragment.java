package com.instahela.deni.mkopo.ui.load.personprofile;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.blankj.utilcode.util.KeyboardUtils;
import com.instahela.deni.mkopo.base.BaseFragment;

public class BaseProfileFragment extends BaseFragment {

    protected void showTimePicker(OnTimeSelectListener listener) {
        if (KeyboardUtils.isSoftInputVisible(getActivity())) {
            KeyboardUtils.hideSoftInput(getActivity());
        }
        //时间选择器
        TimePickerView pvTime = new TimePickerBuilder(getContext(), listener).setSubmitText("ok")
        .setCancelText("cancel").build();
        pvTime.show();
    }
}
