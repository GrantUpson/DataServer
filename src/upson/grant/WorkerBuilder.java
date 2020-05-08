package upson.grant;

/*
  @author Grant Upson : 385831
  @author Adib Shadman : 468684
*/

import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.ServerCreate;
import org.openstack4j.openstack.OSFactory;

import java.util.Base64;

public class WorkerBuilder
{
    private static int workerNumber;
    private OSClient.OSClientV3 os = null;

    public WorkerBuilder()
    {
        os = OSFactory.builderV3()
                .endpoint("https://keystone.rc.nectar.org.au:5000/v3/")
                .credentials("asornob@utas.edu.au", "MjUwZTcwYWZlNWYyNTU3", Identifier.byName("Default"))
                .scopeToProject(Identifier.byId("dbe7e88bf3d046b1be5d8eed6b45e8ab")).authenticate();

        workerNumber = 2;
    }

    public String createWorker()
    {
        String script = Base64.getEncoder().encodeToString(("#!/bin/bash \n" + "sudo /etc/init.d/mysql start \n" +
                "cd /home/ubuntu/ \n" + "java -jar Worker.jar 144.6.231.157 7777").getBytes());

        ServerCreate server = Builders.server().name("Worker-" + workerNumber)
                .flavor("406352b0-2413-4ea6-b219-1a4218fd7d3b")
                .image("1ec99b61-cd90-4991-911d-1821d5d285c7")
                .keypairName("nectarKey")
                .userData(script).build();

        workerNumber++;

        return os.compute().servers().boot(server).getId();
    }

    public void deleteServer(String serverID)
    {
        os.compute().servers().delete("serverID");
    }
}
