package de.dytanic.cloudnet.examples.driver;

import de.dytanic.cloudnet.common.collection.Iterables;
import de.dytanic.cloudnet.common.concurrent.ITask;
import de.dytanic.cloudnet.common.concurrent.ITaskListener;
import de.dytanic.cloudnet.driver.CloudNetDriver;
import de.dytanic.cloudnet.driver.service.GroupConfiguration;
import de.dytanic.cloudnet.driver.service.ServiceConfiguration;
import de.dytanic.cloudnet.driver.service.ServiceTemplate;

public final class ExampleGroups {

    public void test() {
        addGroupConfiguration();
        updateGroupConfiguration();
        removeGroupConfiguration();
    }

    public void addGroupConfiguration() {
        CloudNetDriver.getInstance().addGroupConfiguration(new GroupConfiguration("TestGroup")); //Creates a group without default includes, templates and deployments
    }

    public void updateGroupConfiguration() {
        if (CloudNetDriver.getInstance().isGroupConfigurationPresent("TestGroup")) {
            CloudNetDriver.getInstance().getGroupConfigurationAsync("TestGroup").addListener(new ITaskListener<GroupConfiguration>() {
                @Override
                public void onComplete(ITask<GroupConfiguration> task, GroupConfiguration result) {
                    result.getTemplates().add(new ServiceTemplate( //add a new ServiceTemplate to the group
                            "Lobby",
                            "default",
                            "local"
                    ));

                    CloudNetDriver.getInstance().addGroupConfiguration(result); //add or update the group configuration
                }
            }).addListener(ITaskListener.FIRE_EXCEPTION_ON_FAILURE);
        }
    }

    public void removeGroupConfiguration() {
        CloudNetDriver.getInstance().removeGroupConfiguration("TestGroup"); //remove the group configuration in network
    }

    public void checkGroupConfiguration(ServiceConfiguration serviceConfiguration) {
        if (Iterables.contains("TestGroup", serviceConfiguration.getGroups())) //shortcut for contains element in array as example on group items
        {
            System.out.println("Has group contained");
        }
    }
}