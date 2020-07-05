package io.kt.cloud.tanda.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostPersist;
import javax.persistence.PrePersist;

import io.kt.cloud.tanda.App;
import io.kt.cloud.tanda.event.BillIssued;
import io.kt.cloud.tanda.kafka.KafkaSender;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
@Entity
public class Pay {
		
	@Id
	@GeneratedValue
	private Long payId;						//ID 	: 자동생성
	private Long bookId;					//Book Entity와 relation
	private Long dispatchId; 
	private int	 price;						//결제된 택시요금
	private LocalDateTime lastModifyTime;	//DB INSERT, UPDATE Time으로 @PreUpate Hook에서 셋팅 
	
	@PrePersist
	protected void preDispatch() {
		setLastModifyTime(LocalDateTime.now());
	}
	
	@PostPersist
	protected void payAccepted() throws InterruptedException {

		PayRepository repository = App.applicationContext.getBean(PayRepository.class);

		repository.findById(payId).ifPresent(f -> {

			BillIssued bi = new BillIssued();
			bi.setPayId(f.getPayId());
			bi.setDispatchId(f.getDispatchId());
			bi.setBookId(f.getBookId());
			bi.setPrice(f.getPrice());
			bi.setLastModifyTime(f.getLastModifyTime());

			KafkaSender.pub(bi);
		});

	}
}
