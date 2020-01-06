package com.programwar.grpcclient.client;

import com.programwar.grpcexample.server.Employee;
import com.programwar.grpcexample.server.EmployeeServiceGrpc;
import com.programwar.grpcexample.server.Response;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * Client
 */
public class Client {

    public void CreateEmployee() {
        // 1. Creating a gRPC channel
        final ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 6565) // Set host and port
                .usePlaintext() // This setting should be used in dev. In prod, this should be replaced with
                                // TLS/Certificate
                .build();

        // 2. Create a synchronous stub
        final EmployeeServiceGrpc.EmployeeServiceBlockingStub stub = EmployeeServiceGrpc.newBlockingStub(channel);

        // 3. Prepare request
        final Employee request = Employee.newBuilder()
                                    .setFirstName("First Name")
                                    .build();

        // 4. Call the pay method on the stub
        final Response response = stub.create(request);
        System.out.println(response);

        // Finally close the channel
        channel.shutdown();
    }
}