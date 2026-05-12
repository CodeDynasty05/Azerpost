package com.azerpost.app.service.impl;

import com.azerpost.app.exception.ProcessForbidden;
import com.azerpost.app.exception.ResourceNotFoundException;
import com.azerpost.app.model.entity.ReturnEntity;
import com.azerpost.app.model.entity.Shipment;
import com.azerpost.app.model.dto.ReturnAddRequest;
import com.azerpost.app.model.enums.Status;
import com.azerpost.app.repository.ReturnRepository;
import com.azerpost.app.service.ReturnService;
import com.azerpost.app.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReturnServiceImpl implements ReturnService {
    private final ReturnRepository returnRepository;
    private final ShipmentService shipmentService;

    @Override
    public ReturnEntity initiateReturn(ReturnAddRequest request) {
        Shipment shipment = shipmentService.getShipmentById(request.getShipmentId());
        if(shipment.getStatus() != Status.DELIVERED){
            throw new ProcessForbidden("This shipment is not delivered");
        }
        shipmentService.updateStatus(shipment.getId(), Status.RETURN_REQUESTED,request.getReason());
        ReturnEntity returnEntity = new ReturnEntity();
        returnEntity.setShipment(shipment);
        returnEntity.setReason(request.getReason());
        return returnRepository.save(returnEntity);
    }

    @Override
    public ReturnEntity approveReturn(Long id) {
        ReturnEntity returnEntity = returnRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Return not found"));
        Shipment shipment = returnEntity.getShipment();
        shipmentService.updateStatus(shipment.getId(), Status.RETURN_IN_PROGRESS, null);
        return returnRepository.save(returnEntity);
    }

    @Override
    public ReturnEntity completeReturn(Long id) {
        ReturnEntity returnEntity = returnRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Return not found"));
        Shipment shipment = returnEntity.getShipment();
        shipmentService.updateStatus(shipment.getId(), Status.RETURNED, null);
        return returnRepository.save(returnEntity);
    }

    @Override
    public ReturnEntity getReturn(Long id) {
        return returnRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Return not found"));
    }

    @Override
    public List<ReturnEntity> getAllReturns() {
        return returnRepository.findAll();
    }
}
