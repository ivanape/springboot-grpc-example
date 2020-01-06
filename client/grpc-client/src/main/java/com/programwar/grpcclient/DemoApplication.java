package com.programwar.grpcclient;

import com.programwar.grpcexample.EmployeeId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import com.programwar.grpcexample.Employee;
import com.programwar.grpcexample.EmployeeServiceGrpc;
import com.programwar.grpcexample.Response;

import java.util.Random;

@SpringBootApplication
public class DemoApplication {

	private final static String GRPCServer = "localhost";
	private final static int GRPCPort = 6565;

	public static void main(final String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		runTests();
	}


	public static void runTests() {
        final ManagedChannel channel = ManagedChannelBuilder.forAddress(GRPCServer, GRPCPort)
                .usePlaintext()
				.build();
				
		final EmployeeServiceGrpc.EmployeeServiceBlockingStub stub = EmployeeServiceGrpc.newBlockingStub(channel);

		for(int i = 0; i < 10; i++) {
			Random r = new Random();
			final Employee request = Employee.newBuilder()
					.setId(r.nextInt(100)+1)
					.setFirstName("First Name")
					.setLastName("Last Name")
					.build();
			final Response response = stub.create(request);

			System.out.println(response);
		}

		long lastId = 0;

		for (Employee employee: stub.list(null).getEmployeesList()) {
			System.out.println(employee.toString());
			lastId = employee.getId();
		}

		EmployeeId lastEmployeeId = EmployeeId.newBuilder()
											.setId(lastId)
											.build();

		final Response responseDelete = stub.delete(lastEmployeeId);
		System.out.println(responseDelete);

		channel.shutdown();
    }

}
