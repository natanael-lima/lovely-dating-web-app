export interface ProfileRequest {
    id:number;
    userId:number;
    photo: File | null;
    photoFileName:string;
    location: string;
    gender: string;
    age: number;
    likeGender: string;
    maxAge: number;
    minAge: number;
    timestamp: Date;
}
