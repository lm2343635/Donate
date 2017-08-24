package com.xwkj.donate.service.impl;

import com.xwkj.donate.service.WechaterManager;
import com.xwkj.donate.service.common.ManagerTemplate;
import org.directwebremoting.annotations.RemoteProxy;
import org.springframework.stereotype.Service;

@Service
@RemoteProxy(name = "WechaterManager")
public class WechaterManagerImpl extends ManagerTemplate implements WechaterManager {


}
