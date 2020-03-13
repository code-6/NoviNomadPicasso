package stanislav.tun.novinomad.picasso.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import stanislav.tun.novinomad.picasso.PicassoApp;
import stanislav.tun.novinomad.picasso.persistance.pojos.User;
import stanislav.tun.novinomad.picasso.persistance.services.UserService;
import stanislav.tun.novinomad.picasso.util.ConcurrentHolder;

@Component
public class MySessionListener implements ApplicationListener<HttpSessionDestroyedEvent> {
    Logger log = LoggerFactory.getLogger(MySessionListener.class);

    @Autowired
    ConcurrentHolder holder;

    @Autowired
    AuditorAware auditor;

    @Autowired
    UserService userService;

    @Override
    public void onApplicationEvent(HttpSessionDestroyedEvent event) {
        var currentAuditor = auditor.getCurrentAuditor();
        if(!currentAuditor.isEmpty() && currentAuditor.isPresent()){
            var user = userService.getUser(currentAuditor.get().toString());
            if(!user.isEmpty() && user.isPresent()){
                holder.release(user.get());
            }else{
                log.warn("Unable to get current user! Release entities was not executed.");
            }
        }else{
            log.warn("Unable to get current auditor! Release entities was not executed.");
        }
    }
}
