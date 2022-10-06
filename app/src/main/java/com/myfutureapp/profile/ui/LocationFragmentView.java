package com.myfutureapp.profile.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.profile.model.CityListResponse;

public interface LocationFragmentView extends MasterView {

    void setCityListResponse(CityListResponse cityListResponse);
}
