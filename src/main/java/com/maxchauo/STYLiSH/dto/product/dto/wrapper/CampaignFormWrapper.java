package com.maxchauo.STYLiSH.dto.product.dto.wrapper;

import com.maxchauo.STYLiSH.dto.product.form.admin.CampaignForm;
import lombok.Data;

import java.util.List;

@Data
public class CampaignFormWrapper {
  private List<CampaignForm> campaignForms;
}