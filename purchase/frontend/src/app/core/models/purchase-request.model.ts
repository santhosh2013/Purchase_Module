export interface PurchaseRequestDTO {
  prid?: number;
  eventid: number;
  eventname: string;
  vendorid: number;
  vendorname: string;
  cdsid: string;
  requestdate: string;
  allocatedamount: number;
  prstatus: string;
}

export enum PurchaseRequestStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED'
}
