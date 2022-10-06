package com.myfutureapp.dashboard.application.ui;

import com.myfutureapp.core.ui.MasterView;
import com.myfutureapp.dashboard.application.model.OldApplicationResponse;

public interface OldView extends MasterView {

    void setOldApplicationsResponse(OldApplicationResponse oldApplicationResponse);
}
