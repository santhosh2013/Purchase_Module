export interface PurchaseOrderDTO {
  po_id?: number;
  eventid: number;
  eventname: string;
  vendorid: number;
  vendorname: string;
  cdsid: string;
  orderdate: string;
  orderamountINR: number;
  orderamountdollar: number;
  po_status?: string;
  prid?: number;
  negotiationid?: number;
}

export enum PurchaseOrderStatus {
  PENDING = 'PENDING',
  COMPLETED = 'COMPLETED',
  REJECTED = 'REJECTED'
}
