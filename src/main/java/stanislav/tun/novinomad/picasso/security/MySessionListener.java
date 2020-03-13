package stanislav.tun.novinomad.picasso.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;
import org.springframework.stereotype.Component;
import stanislav.tun.novinomad.picasso.persistance.pojos.User;
import stanislav.tun.novinomad.picasso.persistance.services.UserService;
import stanislav.tun.novinomad.picasso.util.ConcurrentHolder;

@Component
public class MySessionListener implements ApplicationListener<HttpSessionDestroyedEvent> {
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
            }
        }
    }
}
