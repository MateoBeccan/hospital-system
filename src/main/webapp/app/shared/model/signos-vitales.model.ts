import { type IConsulta } from '@/shared/model/consulta.model';

export interface ISignosVitales {
  id?: number;
  fechaHoraRegistro?: Date;
  peso?: number | null;
  talla?: number | null;
  temperatura?: number | null;
  presionArterial?: string | null;
  frecuenciaCardiaca?: number | null;
  frecuenciaRespiratoria?: number | null;
  saturacionOxigeno?: number | null;
  observaciones?: string | null;
  activo?: boolean;
  fechaAlta?: Date;
  fechaBaja?: Date | null;
  consulta?: IConsulta;
}

export class SignosVitales implements ISignosVitales {
  constructor(
    public id?: number,
    public fechaHoraRegistro?: Date,
    public peso?: number | null,
    public talla?: number | null,
    public temperatura?: number | null,
    public presionArterial?: string | null,
    public frecuenciaCardiaca?: number | null,
    public frecuenciaRespiratoria?: number | null,
    public saturacionOxigeno?: number | null,
    public observaciones?: string | null,
    public activo?: boolean,
    public fechaAlta?: Date,
    public fechaBaja?: Date | null,
    public consulta?: IConsulta,
  ) {
    this.activo = this.activo ?? false;
  }
}
