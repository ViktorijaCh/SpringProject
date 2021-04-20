package com.example.demo.repository;

import com.example.demo.model.AP_Size;
import com.example.demo.model.AP_SizeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AP_SizeRepository extends JpaRepository<AP_Size, AP_SizeId> {
    public AP_Size findAP_SizeByArtPrint_IdAndAndSize_Id(Long apid,Long sizeId);
    public List<AP_Size> findAP_SizesBySize_Id(Long id);
//    public void deleteAP_SizesByArtPrint_Id(Long id);
    public List<AP_Size> findAP_SizesByArtPrint_Id(Long apid);

    @Modifying
    @Query("delete from AP_Size aps where aps.artPrint.id=:artPrintId")
    void deleteAP_SizesByArtPrint_Id(@Param("artPrintId") Long artPrintId);
    @Modifying
    @Query("delete from AP_Size aps where aps.artPrint.id=:artPrintId and aps.size.id=:sizeId")
    void deleteAP_SizesByArtPrint_IdAAndSizeId(@Param("artPrintId") Long artPrintId,@Param("sizeId") Long sizeId);

}
