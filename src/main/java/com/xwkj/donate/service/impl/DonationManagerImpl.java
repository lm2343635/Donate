package com.xwkj.donate.service.impl;

import com.xwkj.donate.service.DonationManager;
import com.xwkj.donate.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "DonationManager")
public class DonationManagerImpl extends ManagerTemplate implements DonationManager {
}
