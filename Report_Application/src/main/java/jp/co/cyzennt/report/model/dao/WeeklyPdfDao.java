package jp.co.cyzennt.report.model.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import jp.co.cyzennt.report.model.dao.entity.WeeklyPdfEntity;



public interface WeeklyPdfDao extends JpaRepository<WeeklyPdfEntity ,String>  {

}
