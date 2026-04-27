import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { useDateFormat } from '@/shared/composables';
import useDataUtils from '@/shared/data/data-utils.service';
import { type ITurno } from '@/shared/model/turno.model';

import TurnoService from './turno.service';

export default defineComponent({
  name: 'TurnoDetails',
  setup() {
    const dateFormat = useDateFormat();
    const turnoService = inject('turnoService', () => new TurnoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const turno: Ref<ITurno> = ref({});

    const retrieveTurno = async turnoId => {
      try {
        const res = await turnoService().find(turnoId);
        turno.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.turnoId) {
      retrieveTurno(route.params.turnoId);
    }

    return {
      ...dateFormat,
      alertService,
      turno,

      ...dataUtils,

      previousState,
    };
  },
});
