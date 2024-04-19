export interface PhotoRequest {
    id:number;
    userId:number;
    photo:ArrayBuffer | null;
    photoFileName:string;
}
