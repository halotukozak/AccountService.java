package account.db;

import account.model.Privilege;
import account.model.Role;
import account.db.repository.PrivilegeRepository;
import account.db.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup) return;

        Privilege changePass = createPrivilegeIfNotFound("CHANGE_PASS");
        Privilege getPayment = createPrivilegeIfNotFound("READ_PAYMENT");
        Privilege uploadPayment = createPrivilegeIfNotFound("UPLOAD_PAYMENT");
        Privilege getUser = createPrivilegeIfNotFound("READ_USER");
        Privilege deleteUser = createPrivilegeIfNotFound("DELETE_USER");
        Privilege giveRole = createPrivilegeIfNotFound("GIVE_ROLE");

        List<Privilege> userPrivileges = Arrays.asList(changePass, getPayment);
        List<Privilege> accountantPrivileges = Arrays.asList(changePass, getPayment, uploadPayment);
        List<Privilege> adminPrivileges = Arrays.asList(changePass, getUser, deleteUser, giveRole);

        createRoleIfNotFound("ROLE_USER", userPrivileges);
        createRoleIfNotFound("ROLE_ACCOUNTANT", accountantPrivileges);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}