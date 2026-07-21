export interface RequestStatus {
  loading: boolean;
  error: string;
  success: boolean;
}

export const initialStatus: RequestStatus = {
  loading: false,
  error: '',
  success: false
};

export const loadingStatus: RequestStatus = {
  loading: true,
  error: '',
  success: false
};

export const successStatus: RequestStatus = {
  loading: false,
  error: '',
  success: true
};

export const errorStatus = (message: string): RequestStatus => ({
  loading: false,
  error: message,
  success: false
});
