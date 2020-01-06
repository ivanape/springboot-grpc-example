package com.programwar.grpcserver.services;

import java.util.concurrent.ConcurrentHashMap;

import com.programwar.grpcexample.server.Employee;
import com.programwar.grpcexample.server.EmployeeId;
import com.programwar.grpcexample.server.EmployeeList;
import com.programwar.grpcexample.server.Response;
import com.programwar.grpcexample.server.EmployeeServiceGrpc.EmployeeServiceImplBase;

import org.lognet.springboot.grpc.GRpcService;

import com.programwar.grpcexample.server.Empty;

import io.grpc.stub.StreamObserver;

@GRpcService
public class EmployeeService extends EmployeeServiceImplBase {

    private static ConcurrentHashMap<String, Employee> entities = new ConcurrentHashMap<String, Employee>();
    private static long counter = 2;
  
    @Override
    public void create(final Employee request, final StreamObserver<Response> responseObserver) {

        request.toBuilder().setId(counter);
        entities.put(String.valueOf(counter), request);
        counter = counter + 1;
        responseObserver.onNext(Response.newBuilder().setCode(201).setMessage("CREATED").build());
        responseObserver.onCompleted();
    }

    @Override
    public void list(Empty request,
    StreamObserver<EmployeeList> responseObserver) {
        responseObserver.onNext(EmployeeList.newBuilder().addAllEmployees(entities.values()).build());
        responseObserver.onCompleted();
    }

    @Override
    public void update(final Employee request, final StreamObserver<Response> responseObserver) {
        final String id = String.valueOf(request.getId());
        if (entities.keySet().contains(id)) {
            entities.put(id, request);
            responseObserver.onNext(Response.newBuilder().setCode(200).setMessage("UPDATED").build());
        } else {
            responseObserver.onNext(Response.newBuilder().setCode(404).setMessage("NOT_FOUND").build());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void delete(final EmployeeId request, final StreamObserver<Response> responseObserver) {
        final String id = String.valueOf(request.getId());
      if (entities.keySet().contains(id)) {
        entities.remove(id);
        responseObserver.onNext(Response.newBuilder().setCode(200).setMessage("DELETED").build());
      } else {
        responseObserver.onNext(Response.newBuilder().setCode(404).setMessage("NOT_FOUND").build());
      }
      responseObserver.onCompleted();
    }
  }