package com.myfutureapp.profile.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.profile.model.StateWithRegionResponse;

public interface JobLocationFragmentView extends MasterView {

    void setStateRegionListResponse(StateWithRegionResponse stateWithRegionResponse);
}
