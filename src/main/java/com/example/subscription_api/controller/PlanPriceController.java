        package com.example.subscription_api.controller;

        import com.example.subscription_api.dto.plan_price.PlanPriceRequestDTO;
        import com.example.subscription_api.dto.plan_price.PlanPriceResponseDTO;
        import com.example.subscription_api.service.PlanPriceService;
        import jakarta.validation.Valid;
        import lombok.RequiredArgsConstructor;
        import org.springframework.data.domain.Page;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        import java.util.List;

        @RestController
        @RequestMapping("/api/v1/plan-prices")
        @RequiredArgsConstructor
        public class PlanPriceController {

            private final PlanPriceService planPriceService;

            @PostMapping
            public ResponseEntity<PlanPriceResponseDTO> createPlanPrice(@Valid @RequestBody PlanPriceRequestDTO requestDTO) {
                PlanPriceResponseDTO createdPrice = planPriceService.createPlanPrice(requestDTO);
                return new ResponseEntity<>(createdPrice, HttpStatus.CREATED);
            }

            @GetMapping
            public ResponseEntity<Page<PlanPriceResponseDTO>> getAllPlanPrices(
                    @RequestParam(defaultValue = "0") int page,
                    @RequestParam(defaultValue = "10") int size) {
                return ResponseEntity.ok(planPriceService.getAllPlanPrices(page, size));
            }

            @GetMapping("/{id}")
            public ResponseEntity<PlanPriceResponseDTO> getPlanPriceById(@PathVariable String id) {
                return ResponseEntity.ok(planPriceService.getPlanPriceById(id));
            }

            @PutMapping("/{id}")
            public ResponseEntity<PlanPriceResponseDTO> updatePlanPrice(@PathVariable String id, @Valid @RequestBody PlanPriceRequestDTO requestDTO) {
                return ResponseEntity.ok(planPriceService.updatePlanPrice(id, requestDTO));
            }

            @DeleteMapping("/{id}")
            public ResponseEntity<Void> deletePlanPrice(@PathVariable String id) {
                planPriceService.deletePlanPrice(id);
                return ResponseEntity.noContent().build();
            }
        }